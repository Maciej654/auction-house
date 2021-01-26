package pl.poznan.put.controller.user.page;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
import pl.poznan.put.controller.auction.thumbnail.AuctionThumbnailController;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.callback.Callbacks;
import pl.poznan.put.util.view.loader.ViewLoader;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class UserPageController {
    public enum Type {
        PUBLIC,
        PRIVATE
    }

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

    @Getter
    private final ObjectProperty<User> userProperty = new SimpleObjectProperty<>();

    private final ListProperty<Auction> auctionObservableList =
            new SimpleListProperty<>(FXCollections.observableArrayList());

    private final FilteredList<Auction> auctionFilteredList = new FilteredList<>(auctionObservableList);

    private final BooleanProperty privateUserPageProperty = new SimpleBooleanProperty();

    private void setType(Type type) {
        switch (type) {
            case PUBLIC:
                privateOptionsVBox.setVisible(false);
                privateOptionsVBox.setPrefWidth(0.0);
                break;
            case PRIVATE:
                privateOptionsVBox.setVisible(true);
                privateOptionsVBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
                break;
        }
    }

    @Setter
    private Consumer<Auction> thumbnailCallback = Callbacks::noop;

    private final Map<Long, Parent> thumbnailCache = new HashMap<>();

    @FXML
    private void initialize() {
        HBox.setHgrow(spacePane, Priority.ALWAYS);

        auctionFilteredList.addListener((ListChangeListener<Auction>) change -> {
            while (change.next()) {
                if (change.wasAdded()) change.getAddedSubList().forEach(auction -> {
                    val thumbnail = thumbnailCache.get(auction.getId());
                    if (thumbnail != null) thumbnailsFlowPane.getChildren().add(thumbnail);
                });
                if (change.wasRemoved()) change.getRemoved().forEach(auction -> {
                    val thumbnail = thumbnailCache.get(auction.getId());
                    if (thumbnail != null) thumbnailsFlowPane.getChildren().remove(thumbnail);
                });
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (StringUtils.isBlank(newValue)) auctionFilteredList.setPredicate(null);
            else {
                log.info(newValue);
                val filter = newValue.toLowerCase(Locale.ROOT);
                auctionFilteredList.setPredicate(
                        auction -> auction.getAuctionName().toLowerCase(Locale.ROOT).contains(filter) ||
                                   auction.getItemName().toLowerCase(Locale.ROOT).contains(filter)
                );
            }
        });

        privateUserPageProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) setType(Type.PRIVATE);
            else setType(Type.PUBLIC);
        });

        auctionObservableList.addListener((ListChangeListener<Auction>) change -> {
            while (change.next()) {
                if (change.wasAdded()) change.getAddedSubList().forEach(auction -> {
                    val thumbnail = ViewLoader.getParent(AuctionThumbnailController.class, controller -> {
                        controller.setClickCallback(thumbnailCallback);
                        controller.getAuctionProperty().set(auction);
                    });
                    if (thumbnail != null) thumbnailCache.put(auction.getId(), thumbnail);
                });
                if (change.wasRemoved()) change.getRemoved()
                                               .stream()
                                               .map(Auction::getId)
                                               .forEach(thumbnailCache::remove);
            }
        });

        userProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userLabel.setText(newValue.getFullName());
                if (newValue.getAuctions() != null) auctionObservableList.setAll(newValue.getAuctions());
                searchTextField.setText(StringUtils.EMPTY);
                privateUserPageProperty.set(newValue == CurrentUser.getLoggedInUser());
            }
            else {
                auctionObservableList.clear();
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

    @FXML
    private void logoutButtonClick() {
        log.info("logout");
        CurrentUser.setLoggedInUser(null);
    }
}
