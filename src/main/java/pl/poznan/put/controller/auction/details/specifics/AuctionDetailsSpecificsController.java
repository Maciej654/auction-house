package pl.poznan.put.controller.auction.details.specifics;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.val;
import pl.poznan.put.controller.auction.details.specifics.book.AuctionDetailsBookController;
import pl.poznan.put.controller.auction.details.specifics.car.AuctionDetailsCarController;
import pl.poznan.put.controller.auction.details.specifics.phone.AuctionDetailsPhoneController;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Default;
import pl.poznan.put.model.auction.book.Book;
import pl.poznan.put.model.auction.car.Car;
import pl.poznan.put.model.auction.phone.Phone;
import pl.poznan.put.util.converter.EnumConverterUtils;
import pl.poznan.put.util.view.loader.ViewLoader;

public class AuctionDetailsSpecificsController extends AbstractAuctionDetailsTypeController<Auction> {
    @FXML
    private VBox specificVBox;

    @FXML
    private Label typeLabel;

    @Override
    protected void customizeAuctionDetails(Auction auction) {
        val type = auction.getType();
        typeLabel.setText(EnumConverterUtils.toString(type));
        Parent customizedView;
        switch (type) {
            case BOOK -> customizedView = ViewLoader.getParent(AuctionDetailsBookController.class,
                                                               controller -> controller.getAuctionProperty()
                                                                                       .set((Book) auction)
            );
            case CAR -> customizedView = ViewLoader.getParent(AuctionDetailsCarController.class,
                                                              controller -> controller.getAuctionProperty()
                                                                                      .set((Car) auction)
            );
            case DEFAULT -> customizedView = ViewLoader.getParent(AuctionDetailsDefaultController.class,
                                                                  controller -> controller.getAuctionProperty()
                                                                                          .set((Default) auction)
            );
            case PHONE -> customizedView = ViewLoader.getParent(AuctionDetailsPhoneController.class,
                                                                controller -> controller.getAuctionProperty()
                                                                                        .set((Phone) auction)
            );
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        specificVBox.getChildren().setAll(customizedView);
    }
}
