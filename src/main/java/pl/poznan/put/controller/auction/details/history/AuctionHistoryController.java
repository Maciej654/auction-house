package pl.poznan.put.controller.auction.details.history;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class AuctionHistoryController {
    @FXML
    private TableView<Data> historyTableView;

    @FXML
    private TableColumn<AuctionLog, LocalDateTime> dateTableColumn;

    @FXML
    private TableColumn<AuctionLog, String> descriptionTableColumn;
    @Setter
    private Auction auction;
    private final EntityManager entityManager = EntityManagerProvider.getEntityManager();
    @FXML
    private void initialize() {
        log.info("initialize");

        //dateTableColumn.setCellFactory(column -> new SimpleDateFormatTableCell());
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
       /* historyTableView.getSortOrder().add(dateTableColumn);
        historyTableView.getItems().addListener((ListChangeListener<? super AuctionLog>) change -> {
            if (change.next() && change.getAddedSize() > 0) historyTableView.sort();
        });*/
    }

    public void hello() {
        log.info("hello");
    }

    private static class SimpleDateFormatTableCell extends TableCell<AuctionLog, Long> {
        private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        protected void updateItem(Long item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) setText("");
            else {
                val date = new Date(item);
                setText(FORMAT.format(date));
            }
        }
    }
    @lombok.Data
    @AllArgsConstructor
    public static class Data{
        private LocalDateTime timestamp;
        private String description;
    }
    public void updateHistory(){
        TypedQuery<AuctionLog> query = Objects.requireNonNull(entityManager)
                .createQuery("select auctionlog from AuctionLog auctionlog where auctionlog.auction = :auction ", AuctionLog.class);
        query.setParameter("auction", auction);
        List<Data> resultList = query.getResultStream().map(r -> new Data(r.getTimestamp(), r.getDescription())).collect(Collectors.toList());
        ObservableList<Data> auctionLogs = FXCollections.observableArrayList(resultList);
        historyTableView.setItems(auctionLogs);
    }
}
