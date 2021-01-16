package pl.poznan.put.controller.auction.details;

import javafx.event.ActionEvent;
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
    private AuctionHistoryController     auctionHistoryController;
    @FXML
    private AuctionPhotosController      auctionPhotosController;
    @FXML
    private AuctionBidController         auctionBidController;
    @Setter
    private Auction auction;
    @Setter
    private Runnable keyCallBack;
    @FXML
    private void initialize() {
        log.info("initialize");
        auctionDescriptionController.hello();
        auctionHistoryController.hello();
        auctionPhotosController.hello();
        auctionBidController.hello();
    }

    @FXML
    public void backButtonPressed(ActionEvent actionEvent) {
        keyCallBack.run();
    }

    public void setUp(){
        updateLabels();
        setUpSubviews();
    }

    private void setUpSubviews(){
        auctionBidController.setAuction(auction);
        auctionBidController.setAuctionDetailsController(this);
        auctionHistoryController.setAuction(auction);
        auctionHistoryController.updateHistory();
    }

    public void updateLabels(){
        userLabel.setText(auction.getSeller().getEmail());
        auctionNameLabel.setText(auction.getAuctionName());
        itemName.setText(auction.getItemName());
        auctionPriceLabel.setText(auction.getPrice() + " PLN");
        auctionEndLabel.setText(auction.getEndDate().toString());
    }
    public void updateHistory(){
        auctionHistoryController.updateHistory();
    }
}
