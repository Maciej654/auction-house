package pl.poznan.put.controller.browser;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.controller.auction.thumbnail.AuctionThumbnailCacheChangeListener;
import pl.poznan.put.controller.auction.thumbnail.AuctionThumbnailListChangeListener;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.ad.Ad;
import pl.poznan.put.model.ad.personal.PersonalAd;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.follower.Follower;
import pl.poznan.put.model.user.User;
import pl.poznan.put.model.watch.list.item.WatchListItem;
import pl.poznan.put.util.callback.Callbacks;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class BrowserController {
    @FXML
    private CheckBox followedUsersCheckBox;
    @FXML
    private Pane spacePane;

    @FXML
    private VBox suggestedAuctionsVBox;

    @FXML
    public ChoiceBox<String> watchListChoiceBox;

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

    private Consumer<Auction> showAuctionDetails = Callbacks::noop;

    public void setShowAuctionDetails(Consumer<Auction> showAuctionDetails) {
        this.showAuctionDetails = showAuctionDetails;
        suggestedAuctionsList.addListener(new AuctionThumbnailListChangeListener(showAuctionDetails,
                                                                                 suggestedAuctionsCache));
        addSuggestedAuctions();
    }

    @Setter
    private Consumer<User> ownProfileCallback = Callbacks::noop;

    @FXML
    private void myProfileButtonClick() {
        log.info("my profile");

        val user = CurrentUser.getLoggedInUser();
        ownProfileCallback.accept(user);
    }

    @Setter
    private Runnable backCallback = Callbacks::noop;

    @FXML
    private void backButtonClick() {
        log.info("back");

        backCallback.run();
    }

    @Setter
    Runnable userPageCallback = Callbacks::noop;

    private List<WatchListItem> itemsOnAnyWatchList = new ArrayList<>();

    private List<Follower> followees = new ArrayList<>();

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
            prepareFollowee();
            TypedQuery<Auction> query = em.createQuery("select auction from Auction auction ",
                                                       Auction.class);
            listofAuctions = query.getResultStream()
                                  .filter(Auction::isActive)
                                  .filter(this::filterByWatchList)
                                  .filter(this::filterByName)
                                  .filter(this::filterByType)
                                    .filter(this::filterByFollowers)
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

    private boolean filterByFollowers(Auction auction) {
        if(!followedUsersCheckBox.isSelected()){
            return true;
        }
        return followees.stream()
                .anyMatch(i -> i.getFollowee().getEmail().equals(auction.getSeller().getEmail()));
    }

    private void prepareWatchList() {
        if (em != null) {
            var query = em.createQuery("select item from WatchListItem item where  item.follower = :user",
                                       WatchListItem.class);
            query.setParameter("user", CurrentUser.getLoggedInUser());
            itemsOnAnyWatchList = query.getResultList();

        }
    }
    private void prepareFollowee() {
        if (em != null) {
            var query = em.createQuery("select f from Follower f  where f.follower = :follower",
                    Follower.class);
            query.setParameter("follower", CurrentUser.getLoggedInUser());
            followees = query.getResultList();
        }
    }
    @FXML
    private void initialize() {
        log.info("initialize");

        HBox.setHgrow(spacePane, Priority.ALWAYS);

        category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        seller_column.setCellValueFactory(new PropertyValueFactory<>("seller"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
        end_date_column.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        item_name_column.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        auction_name_column.setCellValueFactory(new PropertyValueFactory<>("AuctionName"));
        details_column.setCellValueFactory(new PropertyValueFactory<>("details"));
        setUpChoiceBox();
        click();

        suggestedAuctionsCache.addListener(new AuctionThumbnailCacheChangeListener(suggestedAuctionsVBox.getChildren()));
    }

    private final ObservableMap<Auction, Parent> suggestedAuctionsCache =
            new SimpleMapProperty<>(FXCollections.observableHashMap());

    private final ObservableList<Auction> suggestedAuctionsList =
            new SimpleListProperty<>(FXCollections.observableArrayList());

    private void addSuggestedAuctions() {
        val user = CurrentUser.getLoggedInUser();
        if (user == null) return;
        user.refreshAds();
        val suggested = user.getAds()
                            .stream()
                            .limit(3)
                            .map(PersonalAd::getAuction)
                            .filter(Objects::nonNull)
                            .filter(Auction::isActive)
                            .collect(Collectors.toList());
        Collections.shuffle(suggested);
        if (suggested.size() < 3 && em != null) {
            val query = em.createNamedQuery(Ad.QUERY_FIND_ALL, Ad.class);
            val ads   = query.getResultList();
            Collections.shuffle(ads);
            ads.stream()
               .map(Ad::getAuction)
               .filter(Objects::nonNull)
               .filter(Auction::isActive)
               .limit(3 - suggested.size())
               .forEach(suggested::add);
        }
        suggestedAuctionsList.setAll(suggested);
    }

    private void setUpChoiceBox() {
        if (em == null) { return; }
        var query = em.createQuery("select distinct item.name from WatchListItem item where item.follower = :user",
                                   String.class);
        query.setParameter("user", CurrentUser.getLoggedInUser());
        List<String>           resultList = query.getResultList();
        ObservableList<String> obs        = FXCollections.observableArrayList((String) null);
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
        if (watchListChoiceBox.getValue() == null) {
            return true;
        }
        for (int i = 0; i < itemsOnAnyWatchList.size(); i++) {
            if (itemsOnAnyWatchList.get(i).getName().equals(watchListChoiceBox.getValue())
                    && itemsOnAnyWatchList.get(i).getAuction().equals(auction)
                    && itemsOnAnyWatchList.get(i).getFollower().getEmail().equals(CurrentUser.getLoggedInUser().getEmail())){
                return true;
            }
        }
        return false;

    }
}

