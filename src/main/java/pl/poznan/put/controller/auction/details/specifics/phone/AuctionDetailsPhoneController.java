package pl.poznan.put.controller.auction.details.specifics.phone;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.auction.details.specifics.AbstractAuctionDetailsTypeController;
import pl.poznan.put.model.auction.phone.Phone;
import pl.poznan.put.util.converter.EnumConverterUtils;

@Slf4j
public class AuctionDetailsPhoneController extends AbstractAuctionDetailsTypeController<Phone> {
    @FXML
    private Label producerLabel;

    @FXML
    private Label screenSizeLabel;

    @FXML
    private Label batteryLabel;

    @FXML
    private Label processorLabel;

    @FXML
    private Label ramLabel;

    @FXML
    private Label osLabel;

    @Override
    protected void customizeAuctionDetails(Phone auction) {
        log.info("customize '{}'", auction.getAuctionName());

        producerLabel.setText(auction.getProducer());
        screenSizeLabel.setText(auction.getScreenSize());
        batteryLabel.setText(auction.getBattery());
        processorLabel.setText(auction.getProcessor());
        ramLabel.setText(auction.getRam());
        osLabel.setText(EnumConverterUtils.toString(auction.getOperatingSystem()));
    }
}
