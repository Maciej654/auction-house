package pl.poznan.put.controller.auction.thumbnail;

import lombok.Setter;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.util.callback.Callbacks;

import java.util.function.Consumer;

public class AuctionThumbnailController {
    @Setter
    private Consumer<Auction> clickCallback = Callbacks::noop;

}
