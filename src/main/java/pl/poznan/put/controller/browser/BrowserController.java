package pl.poznan.put.controller.browser;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import pl.poznan.put.model.auction.Auction.Status;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.callback.Callbacks;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class BrowserController {
    @FXML
    private Pane spacePane;

    @FXML
    private VBox suggestedAuctionsVBox;

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

    @Setter
    private Consumer<Auction> showAuctionDetails;

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
            TypedQuery<Auction> query = em.createQuery("select auction from Auction auction ",
                                                       Auction.class);
            val availableStatuses = Set.of(Status.CREATED, Status.BIDDING);
            listofAuctions = query.getResultStream()
                                  .filter(auction -> availableStatuses.contains(auction.getStatus()))
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
        click();

        suggestedAuctionsList.addListener(new AuctionThumbnailListChangeListener(showAuctionDetails,
                                                                                 suggestedAuctionsCache));
        suggestedAuctionsCache.addListener(new AuctionThumbnailCacheChangeListener(suggestedAuctionsVBox.getChildren()));
        addSuggestedAuctions();
    }

    private final ObservableMap<Auction, Parent> suggestedAuctionsCache =
            new SimpleMapProperty<>(FXCollections.observableHashMap());

    private final ObservableList<Auction> suggestedAuctionsList =
            new SimpleListProperty<>(FXCollections.observableArrayList());

    private void addSuggestedAuctions() {
        val user = CurrentUser.getLoggedInUser();
        if (user == null) return;
        val suggested = user.getAds()
                            .stream()
                            .limit(3)
                            .map(PersonalAd::getAuction)
                            .collect(Collectors.toList());
        Collections.shuffle(suggested);
        if (suggested.size() < 3 && em != null) {
            val query = em.createNamedQuery(Ad.QUERY_FIND_ALL, Ad.class);
            val ads   = query.getResultList();
            Collections.shuffle(ads);
            ads.stream()
               .map(Ad::getAuction)
               .limit(3 - suggested.size())
               .forEach(suggested::add);
        }
        suggestedAuctionsList.setAll(suggested);
    }

    private boolean filterByName(Auction auction) {
        String name = auction_name.getCharacters().toString().toUpperCase();
        if (StringUtils.isEmpty(name)) return true;
        String regex       = String.format(".*%s.*", name);
        String auctionName = auction.getAuctionName().toUpperCase();
        return auctionName.matches(regex);
    }

    private boolean filterByType(Auction auction) {
        String options = auction_type.getCharacters().toString().toUpperCase();
        if (StringUtils.isEmpty(options)) return true;
        String auctionType = auction.getCategory().toUpperCase();
        return Objects.equals(options, auctionType);
    }
}

