package pl.poznan.put.controller.auction.crud.create.specifics.car;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.val;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.empty.NotBlankPropertyValidator;
import pl.poznan.put.logic.common.validation.empty.NotNullPropertyValidator;
import pl.poznan.put.logic.common.validation.number.NumberPropertyValidator;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.car.Car;
import pl.poznan.put.model.auction.car.Car.Condition;
import pl.poznan.put.model.auction.car.Car.Fuel;
import pl.poznan.put.model.auction.car.Car.Transmission;
import pl.poznan.put.util.validation.Validation;

public class AuctionCreateCarController extends AbstractValidatedController {
    @SuppressWarnings("rawtypes")
    @Getter
    private final Auction.AuctionBuilder auctionBuilder = Car.builder();

    @FXML
    private TextField makeTextField;

    @FXML
    private ImageView makeWarning;

    private final BooleanProperty makeValid = new SimpleBooleanProperty();

    @FXML
    private TextField modelTextField;

    @FXML
    private ImageView modelWarning;

    private final BooleanProperty modelValid = new SimpleBooleanProperty();

    @FXML
    private TextField mileageTextField;

    @FXML
    private ImageView mileageWarning;

    private final BooleanProperty mileageValid = new SimpleBooleanProperty();

    @FXML
    private ChoiceBox<Car.Transmission> transmissionChoiceBox;

    @FXML
    private ImageView transmissionWarning;

    private final BooleanProperty transmissionValid = new SimpleBooleanProperty();

    @FXML
    private TextField engineTextField;

    @FXML
    private ImageView engineWarning;

    private final BooleanProperty engineValid = new SimpleBooleanProperty();

    @FXML
    private ChoiceBox<Car.Fuel> fuelChoiceBox;

    @FXML
    private ImageView fuelWarning;

    private final BooleanProperty fuelValid = new SimpleBooleanProperty();

    @FXML
    private ChoiceBox<Car.Condition> conditionChoiceBox;

    @FXML
    private ImageView conditionWarning;

    private final BooleanProperty conditionValid = new SimpleBooleanProperty();

    @Override
    protected void installValidation() {
        // make
        Validation.install(
                makeTextField.textProperty(),
                new NotBlankPropertyValidator("Make"),
                makeValid,
                makeWarning
        );

        // model
        Validation.install(
                modelTextField.textProperty(),
                new NotBlankPropertyValidator("Model"),
                modelValid,
                modelWarning
        );

        // mileage
        Validation.install(
                mileageTextField.textProperty(),
                new NumberPropertyValidator("Mileage") /*TODO change to integer number validator*/,
                mileageValid,
                mileageWarning
        );

        // transmission
        Validation.install(
                transmissionChoiceBox.valueProperty(),
                new NotNullPropertyValidator<>("Transmission"),
                transmissionValid,
                transmissionWarning
        );

        // engine
        Validation.install(
                engineTextField.textProperty(),
                new NotBlankPropertyValidator("Engine"),
                engineValid,
                engineWarning
        );

        // fuel
        Validation.install(
                fuelChoiceBox.valueProperty(),
                new NotNullPropertyValidator<>("Fuel"),
                fuelValid,
                fuelWarning
        );

        // condition
        Validation.install(
                conditionChoiceBox.valueProperty(),
                new NotNullPropertyValidator<>("Condition"),
                conditionValid,
                conditionWarning
        );

        val valid = makeValid
                .and(modelValid)
                .and(mileageValid)
                .and(transmissionValid)
                .and(engineValid)
                .and(fuelValid)
                .and(conditionValid);
        informationValid.bind(valid);
    }

    @Override
    protected void setupInitialValues() {
        setupTextField(makeTextField);
        setupTextField(modelTextField);
        setupTextField(mileageTextField);
        setupChoiceBox(transmissionChoiceBox, Transmission.class);
        setupTextField(engineTextField);
        setupChoiceBox(fuelChoiceBox, Fuel.class);
        setupChoiceBox(conditionChoiceBox, Condition.class);
    }
}
