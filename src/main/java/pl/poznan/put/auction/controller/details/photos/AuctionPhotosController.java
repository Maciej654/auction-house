package pl.poznan.put.auction.controller.details.photos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuctionPhotosController {
    @FXML
    private ImageView currentImageView;
    @FXML
    private Button    prevButton;
    @FXML
    private Button    nextButton;
    @FXML
    private Label     indexLabel;

    @FXML
    private void prevButtonClick() {
        log.info("prevButtonClick");
    }

    @FXML
    private void nextButtonClick() {
        log.info("nextButtonClick");
    }

    @FXML
    private void initialize() {
        log.info("initialize");
    }

    public void hello() {
        log.info("hello");
    }
}
