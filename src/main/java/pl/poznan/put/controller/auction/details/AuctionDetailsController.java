package pl.poznan.put.controller.auction.details;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.auction.details.bid.AuctionBidController;
import pl.poznan.put.controller.auction.details.description.AuctionDescriptionController;
import pl.poznan.put.controller.auction.details.history.AuctionHistoryController;
import pl.poznan.put.controller.auction.details.photos.AuctionPhotosController;
import pl.poznan.put.model.auction.Auction;

@Slf4j
public class AuctionDetailsController {
    @FXML
    public Label userLabel;

    @FXML
    public Label auctionNameLabel;

    @FXML
    public Label itemName;

    @FXML
    public Label auctionPriceLabel;

    @FXML
    public Label auctionEndLabel;

    @FXML
    public Button backButton;

    @FXML
    private AuctionDescriptionController auctionDescriptionController;

    @FXML
    private AuctionHistoryController auctionHistoryController;

    @FXML
    private AuctionPhotosController auctionPhotosController;

    @FXML
    private AuctionBidController auctionBidController;

    @Setter
    private Runnable             keyCallBack;

    private Auction auction;

    public void setAuction(Auction auction) {
        auctionPhotosController.setPictures(auction.getPictures());
        this.auction = auction;
    }

    @FXML
    public void backButtonPressed() {
        keyCallBack.run();
    }

    public void setLabels() {
        userLabel.setText(auction.getSeller().getEmail());
        auctionNameLabel.setText(auction.getAuctionName());
        itemName.setText(auction.getItemName());
        auctionPriceLabel.setText(auction.getPrice() + " PLN");
        auctionEndLabel.setText(auction.getEndDate().toString());
    }

}
