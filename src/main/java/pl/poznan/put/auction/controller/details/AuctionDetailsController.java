package pl.poznan.put.auction.controller.details;

import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.auction.controller.details.bid.AuctionBidController;
import pl.poznan.put.auction.controller.details.description.AuctionDescriptionController;
import pl.poznan.put.auction.controller.details.history.AuctionHistoryController;
import pl.poznan.put.auction.controller.details.photos.AuctionPhotosController;

@Slf4j
public class AuctionDetailsController {
    @FXML
    private AuctionDescriptionController auctionDescriptionController;
    @FXML
    private AuctionHistoryController     auctionHistoryController;
    @FXML
    private AuctionPhotosController      auctionPhotosController;
    @FXML
    private AuctionBidController         auctionBidController;

    @FXML
    private void initialize() {
        log.info("initialize");
        auctionDescriptionController.hello();
        auctionHistoryController.hello();
        auctionPhotosController.hello();
        auctionBidController.hello();
    }
}
