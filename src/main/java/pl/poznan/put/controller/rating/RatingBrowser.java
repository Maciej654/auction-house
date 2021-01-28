package pl.poznan.put.controller.rating;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.rating.Rating;
import pl.poznan.put.popup.PopUpWindow;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class RatingBrowser {
    @FXML
    public Label                                  avgLabel;
    @FXML
    public TextField                              userEntry;
    @FXML
    public TableView<ReviewData>                  tableview;
    @FXML
    public TableColumn<ReviewData, Integer>       ratingColumn;
    @FXML
    public TableColumn<ReviewData, String>        auctionNameColumn;
    @FXML
    public TableColumn<ReviewData, String>        commentColumn;
    @FXML
    public TableColumn<ReviewData, LocalDateTime> dateColumn;
    @FXML
    public TableColumn<ReviewData, String>        reviewerColumn;

    private static final EntityManager entityManager = EntityManagerProvider.getEntityManager();

    @SuperBuilder
    @Data
    public static class ReviewData {
        private int           rating;
        private String        auction_name;
        private String        reviewer;
        private String        comment;
        private LocalDateTime date;


    }

    @FXML
    public void userEntered(ActionEvent actionEvent) {
        String user = userEntry.getText();


        try {
            double avg = calculateAvg(user);
            avgLabel.setText("average: " + avg);
        }
        catch (NullPointerException e) {
            PopUpWindow.display("no reviews matching this user");
            return;
        }
        ObservableList<ReviewData> reviewsList = reviewsList(user);
        tableview.setItems(reviewsList);
    }

    @FXML
    private void initialize() {
        log.info("initialize");

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        auctionNameColumn.setCellValueFactory(new PropertyValueFactory<>("auction_name"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        reviewerColumn.setCellValueFactory(new PropertyValueFactory<>("reviewer"));
    }

    private double calculateAvg(String user) {
        Query nativeQuery = Objects.requireNonNull(entityManager)
                                   .createNativeQuery("select Subprograms.calculateRating(:p_reviewee) from dual");
        nativeQuery.setParameter("p_reviewee", user);
        return ((BigDecimal) nativeQuery.getSingleResult()).doubleValue();
    }

    private ObservableList<ReviewData> reviewsList(String user) {
        TypedQuery<Rating> query =
                Objects.requireNonNull(entityManager)
                       .createQuery("select rating from Rating rating where rating.auction.seller.email = :seller",
                                    Rating.class);
        query.setParameter("seller", user);
        List<ReviewData> rows = query.getResultStream().map(r -> ReviewData.builder()
                                                                           .rating(r.getRating())
                                                                           .auction_name(r.getAuction()
                                                                                          .getAuctionName())
                                                                           .reviewer(r.getReviewer().getEmail())
                                                                           .comment(r.getComment())
                                                                           .date(r.getAuction().getEndDate())
                                                                           .build()).collect(Collectors.toList());
        return FXCollections.observableArrayList(rows);
    }
}
