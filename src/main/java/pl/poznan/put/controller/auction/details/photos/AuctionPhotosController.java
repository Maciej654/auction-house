package pl.poznan.put.controller.auction.details.photos;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.model.picture.Picture;

import java.io.ByteArrayInputStream;
import java.util.Collection;

@Slf4j
public class AuctionPhotosController {
    @FXML
    private ImageView currImageView;

    @FXML
    private Label indexLabel;

    private final IntegerProperty currIndexProperty = new SimpleIntegerProperty();

    private Image[] images;

    public void setPictures(Collection<Picture> pictures) {
        images = pictures.stream()
                         .map(Picture::getImage)
                         .map(ByteArrayInputStream::new)
                         .map(input -> new Image(input, 500, 500, true, true))
                         .toArray(Image[]::new);
        currIndexProperty.set(-1);
        currIndexProperty.set(0);
    }

    @FXML
    private void prevImageViewClick() {
        log.info("prev");

        if (images != null && images.length > 0) {
            var index = currIndexProperty.get();
            if (index == 0) index = images.length;
            currIndexProperty.set(index - 1);
        }
    }

    @FXML
    private void nextImageViewClick() {
        log.info("next");

        if (images != null && images.length > 0) {
            var index = currIndexProperty.get() + 1;
            if (index == images.length) index = 0;
            currIndexProperty.set(index);
        }
    }

    @FXML
    private void initialize() {
        log.info("initialize");

        currIndexProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            val index = (int) newValue;
            if (
                    images != null &&
                    index > -1 &&
                    index < images.length
            ) {
                currImageView.setImage(images[index]);
                val label = String.format("%d/%d", index + 1, images.length);
                indexLabel.setText(label);
            }
        });
    }
}
