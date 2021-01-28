package pl.poznan.put.controller.auction.details.specifics;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.auction.Auction;

@Slf4j
public abstract class AbstractAuctionDetailsTypeController<T extends Auction> {
    @Getter
    protected final ObjectProperty<T> auctionProperty = new SimpleObjectProperty<>();

    protected abstract void customizeAuctionDetails(T auction);

    @FXML
    protected void initialize() {
        log.info("initialize");

        auctionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) customizeAuctionDetails(newValue);
        });
    }
}
