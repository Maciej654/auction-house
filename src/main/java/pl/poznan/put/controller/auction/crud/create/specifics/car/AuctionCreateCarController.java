package pl.poznan.put.controller.auction.crud.create.specifics.car;

import lombok.Getter;
import pl.poznan.put.model.auction.car.Car;

public class AuctionCreateCarController {
    @Getter
    private final Car.CarBuilder<?, ?> carBuilder = Car.builder();
}
