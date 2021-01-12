package pl.poznan.put.controller.auction.crud.create.specifics;

import lombok.Getter;
import pl.poznan.put.model.auction.Default;

public class AuctionCreateDefaultController {
    @Getter
    private final Default.DefaultBuilder<?, ?> defaultBuilder = Default.builder();
}
