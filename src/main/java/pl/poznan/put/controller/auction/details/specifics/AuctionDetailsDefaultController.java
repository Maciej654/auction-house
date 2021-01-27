package pl.poznan.put.controller.auction.details.specifics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pl.poznan.put.model.auction.Default;

public class AuctionDetailsDefaultController extends AbstractAuctionDetailsTypeController<Default> {
    @FXML
    private Label categoryLabel;

    @Override
    protected void customizeAuctionDetails(Default auction) {
        categoryLabel.setText(auction.getCategory());
    }
}
