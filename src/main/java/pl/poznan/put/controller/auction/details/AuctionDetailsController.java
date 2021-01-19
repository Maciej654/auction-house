package pl.poznan.put.controller.auction.details;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.auction.details.bid.AuctionBidController;
import pl.poznan.put.controller.auction.details.history.AuctionHistoryController;
import pl.poznan.put.controller.auction.details.photos.AuctionPhotosController;
import pl.poznan.put.model.auction.Auction;

@Slf4j
public class AuctionDetailsController {
    @FXML
    private Label userLabel;

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
    private AuctionHistoryController auctionHistoryController;

    @FXML
    private AuctionPhotosController auctionPhotosController;

    @FXML
    private AuctionBidController auctionBidController;

    @Setter
    private Runnable keyCallBack;

    @Getter
    private final ObjectProperty<Auction> auctionProperty = new SimpleObjectProperty<>();

    @FXML
    private void initialize() {
        auctionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userLabel.setText(newValue.getSeller().getEmail());
                auctionNameLabel.setText(newValue.getAuctionName());
                itemNameLabel.setText(newValue.getItemName());
                auctionPriceLabel.setText(newValue.getPrice() + " PLN");
                auctionEndLabel.setText(newValue.getEndDate().toString());
                auctionPhotosController.setPictures(newValue.getPictures());
                descriptionWebView.getEngine().loadContent(newValue.getItemDescription());

                auctionBidController.setAuction(newValue);
                auctionBidController.setAuctionDetailsController(this);
                auctionHistoryController.setAuction(newValue);
                auctionHistoryController.updateHistory();
            }
        });
    }

    @FXML
    public void backButtonPressed() {
        keyCallBack.run();
    }

    public void updateHistory(){
        auctionHistoryController.updateHistory();
    }
}
