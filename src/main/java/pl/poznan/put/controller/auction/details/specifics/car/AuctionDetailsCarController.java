package pl.poznan.put.controller.auction.details.specifics.car;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.auction.details.specifics.AbstractAuctionDetailsTypeController;
import pl.poznan.put.model.auction.car.Car;
import pl.poznan.put.util.converter.EnumConverterUtils;

@Slf4j
public class AuctionDetailsCarController extends AbstractAuctionDetailsTypeController<Car> {
    @FXML
    private Label makeLabel;

    @FXML
    private Label modelLabel;

    @FXML
    private Label mileageLabel;

    @FXML
    private Label transmissionLabel;

    @FXML
    private Label engineLabel;

    @FXML
    private Label fuelLabel;

    @FXML
    private Label conditionLabel;


    @Override
    protected void customizeAuctionDetails(Car auction) {
        log.info("customize '{}'", auction.getAuctionName());

        makeLabel.setText(auction.getMake());
        modelLabel.setText(auction.getModel());
        mileageLabel.setText(Integer.toString(auction.getMileage()));
        transmissionLabel.setText(EnumConverterUtils.toString(auction.getTransmission()));
        engineLabel.setText(auction.getEngine());
        fuelLabel.setText(EnumConverterUtils.toString(auction.getFuel()));
        conditionLabel.setText(EnumConverterUtils.toString(auction.getCondition()));
    }
}
