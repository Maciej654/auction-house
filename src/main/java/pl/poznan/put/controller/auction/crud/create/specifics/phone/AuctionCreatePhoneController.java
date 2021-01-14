package pl.poznan.put.controller.auction.crud.create.specifics.phone;

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
import pl.poznan.put.logic.common.validation.number.PositiveDoublePropertyValidator;
import pl.poznan.put.model.auction.Auction.AuctionBuilder;
import pl.poznan.put.model.auction.phone.Phone;
import pl.poznan.put.model.auction.phone.Phone.OS;
import pl.poznan.put.util.validation.Validation;


public class AuctionCreatePhoneController extends AbstractValidatedController implements AuctionBuilderController {
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
                producerValid,
                producerWarning,
                new NotBlankPropertyValidator("Producer")
        );

        // screen size
        Validation.install(
                screenSizeTextField.textProperty(),
                screenSizeValid,
                screenSizeWarning,
                new PositiveDoublePropertyValidator("Screen size")
        );

        // battery
        Validation.install(
                batteryTextField.textProperty(),
                batteryValid,
                batteryWarning,
                new PositiveDoublePropertyValidator("Battery")
        );

        // processor
        Validation.install(
                processorTextField.textProperty(),
                processorValid,
                processorWarning,
                new NotBlankPropertyValidator("Processor")
        );

        // ram
        Validation.install(
                ramTextField.textProperty(),
                ramValid,
                ramWarning,
                new PositiveDoublePropertyValidator("RAM")
        );

        // os
        Validation.install(
                osChoiceBox.valueProperty(),
                osValid,
                osWarning,
                new NotNullPropertyValidator<>("OS")
        );

        val valid = producerValid
                .and(screenSizeValid)
                .and(batteryValid)
                .and(processorValid)
                .and(ramValid)
                .and(osValid);

        informationValid.bind(valid);
    }

    @Override
    protected void setupInitialValues() {
        setupTextField(producerTextField);
        setupTextField(screenSizeTextField);
        setupTextField(batteryTextField);
        setupTextField(processorTextField);
        setupTextField(ramTextField);
        setupChoiceBox(osChoiceBox, OS.class);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AuctionBuilder getAuctionBuilder() {
        return Phone.builder()
                    .producer(producerTextField.getText())
                    .screenSize(screenSizeTextField.getText())
                    .battery(batteryTextField.getText())
                    .processor(processorTextField.getText())
                    .ram(ramTextField.getText())
                    .operatingSystem(osChoiceBox.getValue());
    }
}
