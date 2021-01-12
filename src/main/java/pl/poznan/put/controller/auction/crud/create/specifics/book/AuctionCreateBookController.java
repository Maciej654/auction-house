package pl.poznan.put.controller.auction.crud.create.specifics.book;

import lombok.Getter;
import pl.poznan.put.model.auction.book.Book;

public class AuctionCreateBookController {
    @Getter
    private final Book.BookBuilder<?, ?> bookBuilder = Book.builder();
}
