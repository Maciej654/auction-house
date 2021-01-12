package pl.poznan.put.controller.auction.crud.create.specifics;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.AbstractValidatedController;
import pl.poznan.put.controller.auction.crud.create.specifics.book.AuctionCreateBookController;
import pl.poznan.put.controller.auction.crud.create.specifics.car.AuctionCreateCarController;
import pl.poznan.put.controller.auction.crud.create.specifics.phone.AuctionCreatePhoneController;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Type;
import pl.poznan.put.util.converter.EnumConverter;

public class AuctionCreateSpecificsController extends AbstractValidatedController {
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

    @Override
    protected void installValidation() {
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) switch (newValue) {
                case BOOK:
                    auctionBuilder = auctionCreateBookController.getBookBuilder();

                    informationValid.unbind();
                    informationValid.bind(auctionCreateBookController.getInformationValid());

                    bookVBox.setVisible(true);
                    carVBox.setVisible(false);
                    defaultVBox.setVisible(false);
                    phoneVBox.setVisible(false);

                    bookVBox.toFront();
                    break;
                case CAR:
                    auctionBuilder = auctionCreateCarController.getCarBuilder();

                    informationValid.unbind();
                    informationValid.bind(auctionCreateCarController.getInformationValid());

                    bookVBox.setVisible(false);
                    carVBox.setVisible(true);
                    defaultVBox.setVisible(false);
                    phoneVBox.setVisible(false);

                    carVBox.toFront();
                    break;
                case DEFAULT:
                    auctionBuilder = auctionCreateDefaultController.getDefaultBuilder();

                    informationValid.unbind();
                    informationValid.bind(auctionCreateDefaultController.getInformationValid());

                    bookVBox.setVisible(false);
                    carVBox.setVisible(false);
                    defaultVBox.setVisible(true);
                    phoneVBox.setVisible(false);

                    defaultVBox.toFront();
                    break;
                case PHONE:
                    auctionBuilder = auctionCreatePhoneController.getPhoneBuilder();

                    informationValid.unbind();
                    informationValid.bind(auctionCreatePhoneController.getInformationValid());

                    bookVBox.setVisible(false);
                    carVBox.setVisible(false);
                    defaultVBox.setVisible(false);
                    phoneVBox.setVisible(true);

                    phoneVBox.toFront();
                    break;
            }
        });
    }

    @Override
    protected void setupInitialValues() {
        val items = FXCollections.observableArrayList(Auction.Type.values());
        typeChoiceBox.setConverter(new EnumConverter<>(Auction.Type.class));
        typeChoiceBox.setItems(items);
        typeChoiceBox.setValue(Type.DEFAULT);
    }
}
