package pl.poznan.put.controller.user.page;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.user.User;

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

    private User user;

    public void setUser(User user) {
        this.user = user;
        updateView();
    }

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

    private void updateView() {
        userLabel.setText(user.getFullName());
    }

    @FXML
    private void initialize() {
        HBox.setHgrow(spacePane, Priority.ALWAYS);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            log.info(newValue);
            //TODO filter auctions that match the `newValue`
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
    private Consumer<User> editCallback = (user) -> {};

    @FXML
    private void editProfileButtonClick() {
        log.info("edit");
        editCallback.accept(user);
    }

    @Setter
    private Consumer<User> createAuctionCallback = (user) -> {};

    @FXML
    private void createAuctionButtonClick() {
        log.info("create auction");
        if (user != null) createAuctionCallback.accept(user);
    }
}