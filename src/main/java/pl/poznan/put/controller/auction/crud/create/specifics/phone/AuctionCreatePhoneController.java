package pl.poznan.put.controller.auction.crud.create.specifics.phone;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.empty.NotBlankPropertyValidator;
import pl.poznan.put.logic.common.validation.empty.NotNullPropertyValidator;
import pl.poznan.put.logic.common.validation.number.NumberPropertyValidator;
import pl.poznan.put.model.auction.phone.Phone;
import pl.poznan.put.model.auction.phone.Phone.OS;
import pl.poznan.put.util.converter.EnumConverter;
import pl.poznan.put.util.validation.Validation;


public class AuctionCreatePhoneController extends AbstractValidatedController {
    @Getter
    private final Phone.PhoneBuilder<?, ?> phoneBuilder = Phone.builder();

    @FXML
    private TextField producerTextField;

    @FXML
    private ImageView producerWarning;

    private final BooleanProperty producerValid = new SimpleBooleanProperty();

    @FXML
    private TextField screenSizeTextField;

    @FXML
    private ImageView screenSizeWarning;

    private final BooleanProperty screenSizeValid = new SimpleBooleanProperty();

    @FXML
    private TextField batteryTextField;

    @FXML
    private ImageView batteryWarning;

    private final BooleanProperty batteryValid = new SimpleBooleanProperty();

    @FXML
    private TextField processorTextField;

    @FXML
    private ImageView processorWarning;

    private final BooleanProperty processorValid = new SimpleBooleanProperty();

    @FXML
    private TextField ramTextField;

    @FXML
    private ImageView ramWarning;

    private final BooleanProperty ramValid = new SimpleBooleanProperty();

    @FXML
    private ChoiceBox<Phone.OS> osChoiceBox;

    @FXML
    private ImageView osWarning;

    private final BooleanProperty osValid = new SimpleBooleanProperty();

    @Override
    protected void installValidation() {
        // producer
        Validation.install(
                producerTextField.textProperty(),
                new NotBlankPropertyValidator("Producer"),
                producerValid,
                producerWarning
        );

        // screen size
        Validation.install(
                screenSizeTextField.textProperty(),
                new NumberPropertyValidator("Screen size"),
                screenSizeValid,
                screenSizeWarning
        );

        // battery
        Validation.install(
                batteryTextField.textProperty(),
                new NumberPropertyValidator("Battery"),
                batteryValid,
                batteryWarning
        );

        // processor
        Validation.install(
                processorTextField.textProperty(),
                new NotBlankPropertyValidator("Processor"),
                processorValid,
                processorWarning
        );

        // ram
        Validation.install(
                ramTextField.textProperty(),
                new NumberPropertyValidator("RAM"),
                ramValid,
                ramWarning
        );

        Validation.install(
                osChoiceBox.valueProperty(),
                new NotNullPropertyValidator<>("OS"),
                osValid,
                osWarning
        );
    }

    @Override
    protected void setupInitialValues() {
        producerTextField.setText(null);
        screenSizeTextField.setText(null);
        batteryTextField.setText(null);
        processorTextField.setText(null);
        ramTextField.setText(null);

        val items = FXCollections.observableArrayList(OS.values());
        osChoiceBox.setConverter(new EnumConverter<>(OS.class));
        osChoiceBox.setItems(items);
        osChoiceBox.setValue(null);
    }
}
