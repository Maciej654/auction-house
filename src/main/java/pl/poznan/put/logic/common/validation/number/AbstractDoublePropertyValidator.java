package pl.poznan.put.logic.common.validation.number;

import lombok.val;
import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;
import pl.poznan.put.util.converter.DoubleConverterUtils;

import java.util.function.Predicate;

abstract public class AbstractDoublePropertyValidator extends AbstractPropertyValidator<String> {
    private final Predicate<Double> check;

    protected AbstractDoublePropertyValidator(String field, Predicate<Double> check) {
        super(field);
        this.check = check;
    }

    @Override
    public Predicate<String> getPredicate() {
        return s -> {
            try {
                val value = DoubleConverterUtils.fromString(s);
                return check.test(value);
            }
            catch (NumberFormatException | NullPointerException ignored) {
                return false;
            }
        };
    }

    @Override
    public String getErrorMessage() {
        return field + " must be a double";
    }
}
