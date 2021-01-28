package pl.poznan.put.controller.auction.details.specifics.book;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.auction.details.specifics.AbstractAuctionDetailsTypeController;
import pl.poznan.put.model.auction.book.Book;
import pl.poznan.put.util.converter.EnumConverterUtils;

@Slf4j
public class AuctionDetailsBookController extends AbstractAuctionDetailsTypeController<Book> {
    @FXML
    private Label authorLabel;

    @FXML
    private Label coverLabel;

    @FXML
    private Label genreLabel;

    @Override
    protected void customizeAuctionDetails(Book auction) {
        log.info("customize '{}'", auction.getAuctionName());

        authorLabel.setText(auction.getAuthor());
        coverLabel.setText(EnumConverterUtils.toString(auction.getCover()));
        genreLabel.setText(auction.getGenre());
    }
}
