package pl.poznan.put.controller.user.page;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
public class UserPageController {
    public enum Type {
        PUBLIC,
        PRIVATE
    }

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

    private final ObservableList<Auction> auctionObservableList = FXCollections.observableArrayList();

    private final FilteredList<Auction> auctionFilteredList = new FilteredList<>(auctionObservableList);


    public void setType(Type type) {
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

    @FXML
    private void initialize() {
        HBox.setHgrow(spacePane, Priority.ALWAYS);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            log.info(newValue);
            if (StringUtils.isBlank(newValue)) auctionFilteredList.setPredicate(null);
            else {
                val filter = newValue.toLowerCase(Locale.ROOT);
                auctionFilteredList.setPredicate(
                        auction -> auction.getAuctionName().toLowerCase(Locale.ROOT).contains(filter) ||
                                   auction.getItemName().toLowerCase(Locale.ROOT).contains(filter)
                );
            }
        });

        userProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userLabel.setText(newValue.getFullName());
                auctionObservableList.setAll(newValue.getAuctions());
                searchTextField.setText(StringUtils.EMPTY);
            }
        });
    }

    @Setter
    private Runnable auctionsCallback = () -> {};

    @FXML
    private void auctionsButtonClick() {
        log.info("auctions");
        auctionsCallback.run();
    }

    @Setter
    private Consumer<User> editCallback = user -> {};

    @FXML
    private void editProfileButtonClick() {
        log.info("edit");
        val user = userProperty.get();
        if (user != null) editCallback.accept(user);
    }

    @Setter
    private Consumer<User> createAuctionCallback = user -> {};

    @FXML
    private void createAuctionButtonClick() {
        log.info("create auction");
        val user = userProperty.get();
        if (user != null) createAuctionCallback.accept(user);
    }

    @Setter
    private Runnable logoutCallback = () -> {};

    @FXML
    private void logoutButtonClick() {
        log.info("logout");
        logoutCallback.run();
    }
}
