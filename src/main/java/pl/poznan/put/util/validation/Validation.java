package pl.poznan.put.util.validation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import lombok.experimental.UtilityClass;
import lombok.val;
import pl.poznan.put.logic.common.validation.PropertyValidator;

import java.util.function.Predicate;

@UtilityClass
public class Validation {
    public <R> void install(
            ObservableValue<R> value,
            BooleanProperty property,
            ImageView warning,
            PropertyValidator<R> validator) {
        install(
                value,
                property,
                warning,
                validator.getErrorMessage(),
                validator.getPredicate()
        );
    }

    public <R> void install(
            ObservableValue<R> value,
            BooleanProperty property,
            ImageView warning,
            String message,
            Predicate<R> validator) {
        value.addListener((observable, oldValue, newValue) -> {
            val valid = validator.test(newValue);
            warning.setVisible(!valid);
            property.set(valid);
        });
        val tooltip = new Tooltip(message);
        tooltip.setPrefWidth(200);
        tooltip.setWrapText(true);
        Tooltip.install(warning, tooltip);
    }
}
