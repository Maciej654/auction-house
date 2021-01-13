package pl.poznan.put.controller.auction.crud.create.specifics;

import pl.poznan.put.model.auction.Auction;

public interface AuctionBuilderController {
    @SuppressWarnings("rawtypes")
    Auction.AuctionBuilder getAuctionBuilder();
}
