package pl.poznan.put.controller.auction.crud.create;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import lombok.Getter;

public abstract class AbstractValidatedController {
    @Getter
    protected BooleanProperty informationValid = new SimpleBooleanProperty();

    protected abstract void installValidation();

    protected abstract void setupInitialValues();

    @FXML
    protected void initialize() {
        installValidation();
        setupInitialValues();
    }
}
