package pl.poznan.put.logic.auction.exception;

import lombok.Getter;

public class AuctionAlreadyExistsException extends RuntimeException {
    @Getter
    private final String message;

    public AuctionAlreadyExistsException(String name, String item) {
        message = String.format("Auction with name '%s' and item '%s' already exists", name, item);
    }
}
