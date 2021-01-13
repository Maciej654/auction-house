package pl.poznan.put.controller.auction.crud.create.specifics;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.controller.auction.crud.create.specifics.book.AuctionCreateBookController;
import pl.poznan.put.controller.auction.crud.create.specifics.car.AuctionCreateCarController;
import pl.poznan.put.controller.auction.crud.create.specifics.phone.AuctionCreatePhoneController;
import pl.poznan.put.logic.common.validation.empty.NotNullPropertyValidator;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Type;
import pl.poznan.put.util.validation.Validation;

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

    @FXML
    private ImageView typeWarning;

    private final BooleanProperty typeValid = new SimpleBooleanProperty();

    @SuppressWarnings("rawtypes")
    @Getter
    private Auction.AuctionBuilder auctionBuilder;

    @Override
    protected void installValidation() {
        Validation.install(
                typeChoiceBox.valueProperty(),
                new NotNullPropertyValidator<>("Type"),
                typeValid,
                typeWarning
        );
    }

    @Override
    protected void setupInitialValues() {
        setupChoiceBox(typeChoiceBox, Type.class);
    }

    @Override
    @FXML
    protected void initialize() {
        super.initialize();

        typeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) switch (newValue) {
                case BOOK:
                    auctionBuilder = auctionCreateBookController.getAuctionBuilder();

                    informationValid.unbind();
                    informationValid.bind(auctionCreateBookController.getInformationValid());

                    bookVBox.setVisible(true);
                    carVBox.setVisible(false);
                    defaultVBox.setVisible(false);
                    phoneVBox.setVisible(false);

                    bookVBox.toFront();
                    break;
                case CAR:
                    auctionBuilder = auctionCreateCarController.getAuctionBuilder();

                    informationValid.unbind();
                    informationValid.bind(auctionCreateCarController.getInformationValid());

                    bookVBox.setVisible(false);
                    carVBox.setVisible(true);
                    defaultVBox.setVisible(false);
                    phoneVBox.setVisible(false);

                    carVBox.toFront();
                    break;
                case DEFAULT:
                    auctionBuilder = auctionCreateDefaultController.getAuctionBuilder();

                    informationValid.unbind();
                    informationValid.bind(auctionCreateDefaultController.getInformationValid());

                    bookVBox.setVisible(false);
                    carVBox.setVisible(false);
                    defaultVBox.setVisible(true);
                    phoneVBox.setVisible(false);

                    defaultVBox.toFront();
                    break;
                case PHONE:
                    auctionBuilder = auctionCreatePhoneController.getAuctionBuilder();

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
}
