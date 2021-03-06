package pl.poznan.put.controller.auction.thumbnail;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.util.callback.Callbacks;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;

@Slf4j
public class AuctionThumbnailController {
    @FXML
    private ImageView auctionThumbnail;

    @FXML
    private Label itemNameLabel;

    @FXML
    private Label auctionNameLabel;

    @Setter
    private Consumer<Auction> clickCallback = Callbacks::noop;

    @Getter
    private final ObjectProperty<Auction> auctionProperty = new SimpleObjectProperty<>();

    @FXML
    private void initialize() {
        log.info("initialize");

        auctionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) try {
                auctionNameLabel.setText(newValue.getAuctionName());
                itemNameLabel.setText(newValue.getItemName());
                val color = newValue.isActive() ? Color.GREEN : Color.ORANGE;
                auctionNameLabel.setTextFill(color);
                itemNameLabel.setTextFill(color);
                val data  = newValue.getPictures().get(0).getImage();
                val input = new ByteArrayInputStream(data);
                val image = new Image(input, 128, 128, true, true);
                auctionThumbnail.setImage(image);
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @FXML
    private void thumbnailClick() {
        val auction = auctionProperty.get();
        if (auction != null) {
            val name = auction.getAuctionName();
            log.info("'{}' thumbnail clicked", name);
            clickCallback.accept(auction);
        }
    }
}
