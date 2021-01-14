package pl.poznan.put.logic.common.validation.number;

import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

import java.util.function.Predicate;

public class PositiveIntegerPropertyValidator extends AbstractPropertyValidator<String> {
    public PositiveIntegerPropertyValidator(String field) {
        super(field);
    }

    @Override
    public Predicate<String> getPredicate() {
        return s -> {
            try {
                return Integer.parseInt(s) > 0;
            }
            catch (Exception e) {
                return false;
            }
        };
    }

    @Override
    public String getErrorMessage() {
        return field + " has to be a positive integer number";
    }
}
