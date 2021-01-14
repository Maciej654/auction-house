package pl.poznan.put.logic.common.validation.number;

import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

import java.util.function.Predicate;

public class NumberPropertyValidator extends AbstractPropertyValidator<String> {
    public NumberPropertyValidator(String field) {
        super(field);
    }

    @Override
    public Predicate<String> getPredicate() {
        return s -> {
            try {
                Double.parseDouble(s);
                return true;
            }
            catch (Exception ignored) {
                return false;
            }
        };
    }

    @Override
    public String getErrorMessage() {
        return field + " must be a number";
    }
}
