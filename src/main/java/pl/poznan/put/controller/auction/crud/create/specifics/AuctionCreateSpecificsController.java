package pl.poznan.put.controller.auction.crud.create.specifics;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.controller.auction.crud.create.specifics.book.AuctionCreateBookController;
import pl.poznan.put.controller.auction.crud.create.specifics.car.AuctionCreateCarController;
import pl.poznan.put.controller.auction.crud.create.specifics.phone.AuctionCreatePhoneController;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Type;

import java.util.Locale;

public class AuctionCreateSpecificsController {
    @FXML
    private VBox bookVBox;

    @FXML
    private AuctionCreateBookController auctionCreateBookController;

    @FXML
    private VBox carVBox;

    @FXML
    private AuctionCreateCarController auctionCreateCarController;

    @FXML
    private VBox defaultVBox;

    @FXML
    private AuctionCreateDefaultController auctionCreateDefaultController;

    @FXML
    private VBox phoneVBox;

    @FXML
    private AuctionCreatePhoneController auctionCreatePhoneController;

    @FXML
    private ChoiceBox<Auction.Type> typeChoiceBox;

    @Getter
    private Auction.AuctionBuilder<?, ?> auctionBuilder;

    @FXML
    private void initialize() {
        typeChoiceBox.setConverter(new AuctionTypeConverter());
        val items = FXCollections.observableArrayList(Auction.Type.values());
        typeChoiceBox.setItems(items);
        typeChoiceBox.setValue(Type.DEFAULT);
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) switch (newValue) {
                case BOOK:
                    auctionBuilder = auctionCreateBookController.getBookBuilder();
                    bookVBox.setVisible(true);
                    bookVBox.toFront();
                    carVBox.setVisible(false);
                    defaultVBox.setVisible(false);
                    phoneVBox.setVisible(false);
                    break;
                case CAR:
                    auctionBuilder = auctionCreateCarController.getCarBuilder();
                    bookVBox.setVisible(false);
                    carVBox.setVisible(true);
                    carVBox.toFront();
                    defaultVBox.setVisible(false);
                    phoneVBox.setVisible(false);
                    break;
                case DEFAULT:
                    auctionBuilder = auctionCreateDefaultController.getDefaultBuilder();
                    bookVBox.setVisible(false);
                    carVBox.setVisible(false);
                    defaultVBox.setVisible(true);
                    defaultVBox.toFront();
                    phoneVBox.setVisible(false);
                    break;
                case PHONE:
                    auctionBuilder = auctionCreatePhoneController.getPhoneBuilder();
                    bookVBox.setVisible(false);
                    carVBox.setVisible(false);
                    defaultVBox.setVisible(false);
                    phoneVBox.setVisible(true);
                    phoneVBox.toFront();
                    break;
            }
        });
    }

    private static class AuctionTypeConverter extends StringConverter<Auction.Type> {

        @Override
        public String toString(Type object) {
            return StringUtils.capitalize(object.name());
        }

        @Override
        public Type fromString(String string) {
            return Type.valueOf(string.toUpperCase(Locale.ROOT));
        }
    }
}
