package pl.poznan.put.controller.common;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.util.converter.EnumConverter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractValidatedController {
    @Getter
    protected BooleanProperty informationValid = new SimpleBooleanProperty();

    protected abstract void installValidation();

    protected abstract void setupInitialValues();

    protected void setupTextField(TextField textField) {
        textField.setText(null);
    }

    protected <T extends Enum<T>> void setupChoiceBox(ChoiceBox<T> choiceBox, Class<T> clazz) {
        val sorted = Arrays.stream(clazz.getEnumConstants())
                           .sorted(Comparator.comparing(T::name))
                           .collect(Collectors.toList());
        val items = FXCollections.observableArrayList(sorted);
        choiceBox.setConverter(new EnumConverter<>(clazz));
        choiceBox.setItems(items);
        choiceBox.setValue(null);
    }

    @FXML
    protected void initialize() {
        log.info("initialize");

        installValidation();
        setupInitialValues();
    }
}
