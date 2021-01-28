package pl.poznan.put.controller.user.page;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.controller.auction.thumbnail.AuctionThumbnailCacheChangeListener;
import pl.poznan.put.controller.auction.thumbnail.AuctionThumbnailListChangeListener;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.callback.Callbacks;

import java.util.Collection;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class UserPageController {
    @FXML
    private Button followButton;

    @FXML
    private VBox publicOptionsVBox;

    @FXML
    private FlowPane thumbnailsFlowPane;

    @FXML
    private VBox privateOptionsVBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Pane spacePane;

    @FXML
    private ScrollPane auctionsScrollPane;

    @FXML
    private Label userLabel;

    @FXML
    private void followButtonClick() {}

    private enum Options {
        PUBLIC,
        PRIVATE
    }

    @FXML
    private Button shoppingCartButton;

    @FXML
    private Button followerButton;

    @FXML
    private Button reviewCreatorButton;

    @FXML
    private Button reviewBrowserButton;

    @FXML
    private Button deliveryButton;

    @FXML
    private Button historyButton;

    @Getter
    private final ObjectProperty<User> userProperty = new SimpleObjectProperty<>();

    private final ObservableList<Auction> auctionObservableList =
            new SimpleListProperty<>(FXCollections.observableArrayList());

    private void setOptions(Options options) {
        switch (options) {
            case PUBLIC -> {
                privateOptionsVBox.setVisible(false);
                publicOptionsVBox.setVisible(true);
                publicOptionsVBox.toFront();
            }
            case PRIVATE -> {
                publicOptionsVBox.setVisible(false);
                privateOptionsVBox.setVisible(true);
                privateOptionsVBox.toFront();
            }
        }
    }

    public void setThumbnailCallback(Consumer<Auction> callback) {
        auctionObservableList.addListener(new AuctionThumbnailListChangeListener(callback, thumbnailCache));
    }

    @Setter
    private Runnable reviewCreatorCallback = Callbacks::noop;

    @Setter
    private Runnable reviewBrowserCallback = Callbacks::noop;

    @Setter
    private Runnable shoppingCartBrowserCallback = Callbacks::noop;

    @Setter
    private Runnable followerCreatorCallback = Callbacks::noop;

    @Setter
    private Runnable historyCallback = Callbacks::noop;

    @Setter
    private Runnable deliveryCallback = Callbacks::noop;

    private final ObservableMap<Auction, Parent> thumbnailCache =
            new SimpleMapProperty<>(FXCollections.observableHashMap());

    private final ObjectProperty<Predicate<Auction>> auctionPredicateProperty = new SimpleObjectProperty<>();

    @FXML
    private void initialize() {
        log.info("initialize");
        shoppingCartButton.setOnAction(a -> System.out.println("shoppingCartButton"));
        followerButton.setOnAction(a -> System.out.println("followerButton"));
        reviewBrowserButton.setOnAction(a -> System.out.println("reviewBrowserButton"));
        reviewCreatorButton.setOnAction(a -> System.out.println("reviewCreatorButton"));

        reviewCreatorButton.setOnAction(a -> reviewCreatorCallback.run());
        shoppingCartButton.setOnAction(a -> shoppingCartBrowserCallback.run());
        reviewBrowserButton.setOnAction(a -> reviewBrowserCallback.run());
        followerButton.setOnAction(a -> followerCreatorCallback.run());
        historyButton.setOnAction(a -> historyCallback.run());
        deliveryButton.setOnAction(a -> deliveryCallback.run());

        HBox.setHgrow(spacePane, Priority.ALWAYS);

        auctionPredicateProperty.addListener((observable, oldValue, newValue) -> {
            Collection<Parent> thumbnails;
            if (newValue == null) thumbnails = thumbnailCache.values();
            else thumbnails = thumbnailCache.entrySet()
                                            .stream()
                                            .filter(entry -> newValue.test(entry.getKey()))
                                            .map(Entry::getValue)
                                            .collect(Collectors.toList());
            thumbnailsFlowPane.getChildren().setAll(thumbnails);
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (StringUtils.isBlank(newValue)) auctionPredicateProperty.set(null);
            else {
                log.info(newValue);
                val filter = newValue.toLowerCase(Locale.ROOT);
                auctionPredicateProperty.set(
                        auction -> auction.getAuctionName().toLowerCase(Locale.ROOT).contains(filter) ||
                                   auction.getItemName().toLowerCase(Locale.ROOT).contains(filter)
                );
            }
        });

        thumbnailCache.addListener(new AuctionThumbnailCacheChangeListener(thumbnailsFlowPane.getChildren()));

        userProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userLabel.setText(newValue.getFullName());
                newValue.refreshAuctions();
                val current     = CurrentUser.getLoggedInUser();
                val privatePage = Objects.equals(newValue, current);
                val auctions = newValue.getAuctions()
                                       .stream()
                                       .filter(auction -> privatePage || auction.isActive())
                                       .sorted(Auction.LATEST_FIRST)
                                       .collect(Collectors.toList());
                log.info("found {} auctions", auctions.size());
                auctionObservableList.setAll(auctions);
                searchTextField.setText(StringUtils.EMPTY);
                setOptions(privatePage ? Options.PRIVATE : Options.PUBLIC);
            }
        });
    }

    @Setter
    private Runnable auctionsCallback = Callbacks::noop;

    @FXML
    private void auctionsButtonClick() {
        log.info("auctions");
        auctionsCallback.run();
    }

    @Setter
    private Consumer<User> editCallback = Callbacks::noop;

    @FXML
    private void editProfileButtonClick() {
        log.info("edit");
        val user = userProperty.get();
        if (user != null) editCallback.accept(user);
    }

    @Setter
    private Consumer<User> createAuctionCallback = Callbacks::noop;

    @FXML
    private void createAuctionButtonClick() {
        log.info("create auction");
        val user = userProperty.get();
        if (user != null) createAuctionCallback.accept(user);
    }

    @Setter
    private Runnable logoutCallback = Callbacks::noop;

    @FXML
    private void logoutButtonClick() {
        log.info("logout");
        logoutCallback.run();
    }
}
