package pl.poznan.put.controller.auction.details.specifics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.auction.Default;

@Slf4j
public class AuctionDetailsDefaultController extends AbstractAuctionDetailsTypeController<Default> {
    @FXML
    private Label categoryLabel;

    @Override
    protected void customizeAuctionDetails(Default auction) {
        log.info("customize '{}'", auction.getAuctionName());

        categoryLabel.setText(auction.getCategory());
    }
}
