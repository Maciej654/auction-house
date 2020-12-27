package pl.poznan.put.controller.user.page;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.user.User;

@Slf4j
public class UserPage {
    @FXML
    private Label userLabel;

    private User user;

    public void setUser(User user) {
        this.user = user;
        updateView();
    }

    private void updateView() {
        userLabel.setText(user.getFullName());
    }

    @FXML
    private void initialize() {

    }
}
