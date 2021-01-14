package pl.poznan.put.controller.auction.crud.create.specifics.car;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.specifics.AuctionBuilderController;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.empty.NotBlankPropertyValidator;
import pl.poznan.put.logic.common.validation.empty.NotNullPropertyValidator;
import pl.poznan.put.logic.common.validation.number.PositiveIntegerPropertyValidator;
import pl.poznan.put.model.auction.Auction.AuctionBuilder;
import pl.poznan.put.model.auction.car.Car;
import pl.poznan.put.model.auction.car.Car.Condition;
import pl.poznan.put.model.auction.car.Car.Fuel;
import pl.poznan.put.model.auction.car.Car.Transmission;
import pl.poznan.put.util.validation.Validation;

public class AuctionCreateCarController extends AbstractValidatedController implements AuctionBuilderController {
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
                makeValid,
                makeWarning,
                new NotBlankPropertyValidator("Make")
        );

        // model
        Validation.install(
                modelTextField.textProperty(),
                modelValid,
                modelWarning,
                new NotBlankPropertyValidator("Model")
        );

        // mileage
        Validation.install(
                mileageTextField.textProperty(),
                mileageValid,
                mileageWarning,
                new PositiveIntegerPropertyValidator("Mileage")
        );

        // transmission
        Validation.install(
                transmissionChoiceBox.valueProperty(),
                transmissionValid,
                transmissionWarning,
                new NotNullPropertyValidator<>("Transmission")
        );

        // engine
        Validation.install(
                engineTextField.textProperty(),
                engineValid,
                engineWarning,
                new NotBlankPropertyValidator("Engine")
        );

        // fuel
        Validation.install(
                fuelChoiceBox.valueProperty(),
                fuelValid,
                fuelWarning,
                new NotNullPropertyValidator<>("Fuel")
        );

        // condition
        Validation.install(
                conditionChoiceBox.valueProperty(),
                conditionValid,
                conditionWarning,
                new NotNullPropertyValidator<>("Condition")
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

    @SuppressWarnings("rawtypes")
    @Override
    public AuctionBuilder getAuctionBuilder() {
        return Car.builder()
                  .make(makeTextField.getText())
                  .model(modelTextField.getText())
                  .mileage(Integer.parseInt(mileageTextField.getText()));
    }
}
