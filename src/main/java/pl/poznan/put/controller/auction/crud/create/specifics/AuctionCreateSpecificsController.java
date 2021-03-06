package pl.poznan.put.controller.auction.crud.create.specifics;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.auction.crud.create.specifics.book.AuctionCreateBookController;
import pl.poznan.put.controller.auction.crud.create.specifics.car.AuctionCreateCarController;
import pl.poznan.put.controller.auction.crud.create.specifics.phone.AuctionCreatePhoneController;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.empty.NotNullPropertyValidator;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.AuctionBuilder;
import pl.poznan.put.model.auction.Auction.Type;
import pl.poznan.put.util.validation.Validation;

import java.util.function.Supplier;

@Slf4j
public class AuctionCreateSpecificsController extends AbstractValidatedController implements AuctionBuilderController {
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
    private Supplier<Auction.AuctionBuilder> auctionBuilderSupplier;

    @SuppressWarnings("rawtypes")
    @Override
    public AuctionBuilder getAuctionBuilder() {
        return auctionBuilderSupplier == null ? null : auctionBuilderSupplier.get();
    }

    @Override
    protected void installValidation() {
        Validation.install(
                typeChoiceBox.valueProperty(),
                typeValid,
                typeWarning,
                new NotNullPropertyValidator<>("Type")
        );
    }

    @Override
    protected void setupInitialValues() {
        setupChoiceBox(typeChoiceBox, Type.class);
    }

    private <T extends AbstractValidatedController & AuctionBuilderController> void switchTypeContext(
            T controller,
            VBox activeVBox
    ) {
        auctionBuilderSupplier = controller::getAuctionBuilder;

        informationValid.unbind();
        informationValid.bind(controller.getInformationValid());

        bookVBox.setVisible(bookVBox == activeVBox);
        carVBox.setVisible(carVBox == activeVBox);
        defaultVBox.setVisible(defaultVBox == activeVBox);
        phoneVBox.setVisible(phoneVBox == activeVBox);
//        activeVBox.toFront();
    }

    @Override
    @FXML
    protected void initialize() {
        super.initialize();
        log.info("initialize");

        typeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) switch (newValue) {
                case BOOK -> switchTypeContext(auctionCreateBookController, bookVBox);
                case CAR -> switchTypeContext(auctionCreateCarController, carVBox);
                case DEFAULT -> switchTypeContext(auctionCreateDefaultController, defaultVBox);
                case PHONE -> switchTypeContext(auctionCreatePhoneController, phoneVBox);
            }
        });
    }
}
