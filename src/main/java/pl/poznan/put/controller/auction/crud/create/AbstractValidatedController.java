package pl.poznan.put.controller.auction.crud.create;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.val;
import pl.poznan.put.util.converter.EnumConverter;

public abstract class AbstractValidatedController {
    @Getter
    protected BooleanProperty informationValid = new SimpleBooleanProperty();

    protected abstract void installValidation();

    protected abstract void setupInitialValues();

    protected void setupTextField(TextField textField) {
        textField.setText(null);
    }

    protected <T extends Enum<T>> void setupChoiceBox(ChoiceBox<T> choiceBox, Class<T> clazz) {
        val items = FXCollections.observableArrayList(clazz.getEnumConstants());
        choiceBox.setConverter(new EnumConverter<>(clazz));
        choiceBox.setItems(items);
        choiceBox.setValue(null);
    }

    @FXML
    protected void initialize() {
        installValidation();
        setupInitialValues();
    }
}
