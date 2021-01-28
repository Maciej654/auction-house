package pl.poznan.put.controller.browser;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Status;
import pl.poznan.put.model.user.User;
import pl.poznan.put.model.watch.list.item.WatchListItem;
import pl.poznan.put.util.callback.Callbacks;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class BrowserController {

    @FXML
    public ChoiceBox<String> watchListChoiceBox;

    @FXML
    private Button userPageButton;

    @FXML
    private TextField auction_name;
    @FXML
    private TextField auction_type;

    @FXML
    private TableView<Data> table;

    @FXML
    private TableColumn<Data, String> category_column;

    @FXML
    private TableColumn<Data, String> seller_column;

    @FXML
    private TableColumn<Data, Double> price_column;

    @FXML
    private TableColumn<Data, LocalDateTime> end_date_column;

    @FXML
    private TableColumn<Data, String> item_name_column;

    @FXML
    private TableColumn<Data, String> auction_name_column;

    @FXML
    private TableColumn<Data, Button> details_column;

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    @Getter
    private final ObjectProperty<User> userProperty  = new SimpleObjectProperty<>();

    @Setter
    Consumer<Auction> showAuctionDetails;

    @Setter
    Runnable userPageCallback = Callbacks::noop;

    private List<WatchListItem> itemsOnAnyWatchList = new ArrayList<>();

    @SuperBuilder
    @lombok.Data
    public static class Data {
        private String        AuctionName;
        private String        itemName;
        private LocalDateTime endDate;
        private double        price;
        private String        seller;
        private String        category;
        private Button        details;
    }

    private class AuctionDetailsButton extends Button {
        @Getter
        private final Auction auction;

        public AuctionDetailsButton(String text, Auction auction) {
            super(text);
            this.auction = auction;
            this.setOnAction(a -> showAuctionDetails.accept(auction));
        }
    }

    @FXML
    private void click() {

        List<Auction> listofAuctions = new ArrayList<>();
        if (em != null) {
            prepareWatchList();
            TypedQuery<Auction> query             = em.createQuery("select auction from Auction auction ",
                                                                   Auction.class);
            val                 availableStatuses = Set.of(Status.CREATED, Status.BIDDING);
            listofAuctions = query.getResultStream()
                                  .filter(auction -> availableStatuses.contains(auction.getStatus()))
                                  .filter(this::filterByWatchList)
                                  .filter(this::filterByName)
                                  .filter(this::filterByType)
                                  .collect(Collectors.toList());
        }
        List<Data> list = listofAuctions.stream()
                                        .map(auction -> Data.builder()
                                                            .AuctionName(auction.getAuctionName())
                                                            .itemName(auction.getItemName())
                                                            .endDate(auction.getEndDate())
                                                            .price(auction.getPrice())
                                                            .seller(auction.getSeller().getEmail())
                                                            .category(auction.getCategory())
                                                            .details(new AuctionDetailsButton("click here", auction))
                                                            .build()).collect(Collectors.toList());

        ObservableList<Data> obsList = FXCollections.observableArrayList(list);
        table.setItems(obsList);

    }

    private void prepareWatchList() {
        if(em != null){
            var query = em.createQuery("select item from WatchListItem item where  item.follower = :user",WatchListItem.class);
            query.setParameter("user", userProperty.getValue());
            itemsOnAnyWatchList = query.getResultList();
        }
    }


    @FXML
    private void initialize() {
        log.info("initialize");
        userPageButton.setOnAction(a -> userPageCallback.run());
        userProperty.addListener((observable, oldValue, newValue) -> setUpChoiceBox());
        category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        seller_column.setCellValueFactory(new PropertyValueFactory<>("seller"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
        end_date_column.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        item_name_column.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        auction_name_column.setCellValueFactory(new PropertyValueFactory<>("AuctionName"));
        details_column.setCellValueFactory(new PropertyValueFactory<>("details"));
        click();
    }

    private void setUpChoiceBox() {
        if(em == null){ return; }
        var query = em.createQuery("select distinct item.name from WatchListItem item where item.follower = :user",String.class);
        query.setParameter("user",userProperty.get());
        List<String> resultList = query.getResultList();
        ObservableList<String> obs = FXCollections.observableArrayList((String) null);
        obs.addAll(resultList);
        watchListChoiceBox.setItems(obs);


    }

    private boolean filterByName(Auction auction) {
        String name = auction_name.getCharacters().toString().toUpperCase();
        if (StringUtils.isEmpty(name)) return true;
        String regex       = String.format(".*%s.*", name);
        String auctionName = auction.getAuctionName().toUpperCase();
        return auctionName.matches(regex);
    }

    private boolean filterByType(Auction auction) {
        String type = auction_type.getCharacters().toString().toUpperCase();
        if (StringUtils.isEmpty(type)) return true;
        String auctionType = auction.getCategory().toUpperCase();
        return Objects.equals(type, auctionType);
    }
    private boolean filterByWatchList(Auction auction) {
        if(watchListChoiceBox.getValue() == null){
            return true;
        }else if(em != null){
            long count = itemsOnAnyWatchList.stream()
                    .filter(i -> i.getName().equals(watchListChoiceBox.getValue()) &&
                            i.getAuction().equals(auction) &&
                            i.getFollower().equals(userProperty.get())).count();
            return count > 0;
        }
        return false;

    }
}

