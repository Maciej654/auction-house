package pl.poznan.put.controller.auction.details.specifics.book;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pl.poznan.put.controller.auction.details.specifics.AbstractAuctionDetailsTypeController;
import pl.poznan.put.model.auction.book.Book;
import pl.poznan.put.util.converter.EnumConverterUtils;

public class AuctionDetailsBookController extends AbstractAuctionDetailsTypeController<Book> {
    @FXML
    private Label authorLabel;

    @FXML
    private Label coverLabel;

    @FXML
    private Label genreLabel;

    @Override
    protected void customizeAuctionDetails(Book auction) {
        authorLabel.setText(auction.getAuthor());
        coverLabel.setText(EnumConverterUtils.toString(auction.getCover()));
        genreLabel.setText(auction.getGenre());
    }
}
