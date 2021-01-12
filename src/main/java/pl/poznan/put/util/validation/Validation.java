package pl.poznan.put.util.validation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import lombok.experimental.UtilityClass;
import lombok.val;
import pl.poznan.put.logic.common.validation.PropertyValidator;

@UtilityClass
public class Validation {
    public <R> void install(
            ObservableValue<R> value,
            PropertyValidator<R> validator,
            BooleanProperty property,
            ImageView warning) {
        value.addListener((observable, oldValue, newValue) -> {
            val valid = validator.test(newValue);
            warning.setVisible(!valid);
            property.set(valid);
        });
        val message = validator.getErrorMessage();
        val tooltip = new Tooltip(message);
        if (tooltip.getWidth() > 200) {
            tooltip.setPrefWidth(200);
            tooltip.setWrapText(true);
        }
        Tooltip.install(warning, tooltip);
    }
}
