package pl.poznan.put.controller.auction.details;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.controller.auction.details.bid.AuctionBidController;
import pl.poznan.put.controller.auction.details.history.AuctionHistoryController;
import pl.poznan.put.controller.auction.details.photos.AuctionPhotosController;
import pl.poznan.put.controller.auction.details.watchlist.AuctionWatchListController;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.callback.Callbacks;

import java.util.function.Consumer;

@Slf4j
public class AuctionDetailsController {
    @FXML
    private Hyperlink userHyperlink;

    @FXML
    private Label auctionNameLabel;

    @FXML
    private Label itemNameLabel;

    @FXML
    private Label auctionPriceLabel;

    @FXML
    private Label auctionEndLabel;

    @FXML
    private WebView descriptionWebView;

    @FXML
    private AuctionWatchListController auctionWatchListController;

    @FXML
    private AuctionHistoryController auctionHistoryController;

    @FXML
    private AuctionPhotosController auctionPhotosController;

    @FXML
    private AuctionBidController auctionBidController;

    @Setter
    private Consumer<User> backCallback;

    @Getter
    private final ObjectProperty<Auction> auctionProperty = new SimpleObjectProperty<>();

    @Getter
    private final ObjectProperty<User> userProperty = new SimpleObjectProperty<>();

    @FXML
    private void initialize() {
        auctionBidController.getAuctionProperty().bind(auctionProperty);
        auctionBidController.setAfterBidCallback(() -> {
            updateLabels();
            updateHistory();
        });

        auctionHistoryController.getAuctionProperty().bind(auctionProperty);

        auctionWatchListController.getAuctionProperty().bind(auctionProperty);
        auctionWatchListController.getUserProperty().bind(userProperty);

        auctionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userHyperlink.setText(newValue.getSeller().getFullName());
                auctionNameLabel.setText(newValue.getAuctionName());
                itemNameLabel.setText(newValue.getItemName());
                auctionPriceLabel.setText(newValue.getPrice() + " PLN");
                auctionEndLabel.setText(newValue.getEndDate().toString());
                auctionPhotosController.setPictures(newValue.getPictures());
                descriptionWebView.getEngine().loadContent(newValue.getItemDescription());
            }
        });

        userHyperlink.setVisited(true);
    }

    @FXML
    private void backButtonClick() {
        backCallback.accept(userProperty.get());
    }

    public void updateLabels() {
        val auction = auctionProperty.get();
        if (auction != null) {
            auctionPriceLabel.setText(auction.getPrice() + " PLN");
            auctionEndLabel.setText(auction.getEndDate().toString());
        }
    }

    public void updateHistory() {
        auctionHistoryController.updateHistory();
    }

    @Setter
    private Consumer<User> userHyperlinkCallback = Callbacks::noop;

    @FXML
    private void userHyperlinkClick() {
        val auction = auctionProperty.get();
        if (auction == null) return;
        val user = auction.getSeller();
        if (user == null) return;
        log.info("user hyperlink click: '{}'", user.getEmail());
        userHyperlinkCallback.accept(user);
    }
}
