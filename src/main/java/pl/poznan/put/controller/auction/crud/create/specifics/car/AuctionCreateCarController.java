package pl.poznan.put.controller.auction.crud.create.specifics.car;

import javafx.fxml.FXML;
import lombok.Getter;
import pl.poznan.put.controller.auction.crud.create.AbstractValidatedController;
import pl.poznan.put.model.auction.car.Car;

public class AuctionCreateCarController extends AbstractValidatedController {
    @Getter
    private final Car.CarBuilder<?, ?> carBuilder = Car.builder();

    @Override
    protected void installValidation() {

    }

    @Override
    protected void setupInitialValues() {

    }
}
