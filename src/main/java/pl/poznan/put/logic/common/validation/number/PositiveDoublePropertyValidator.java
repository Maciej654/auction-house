package pl.poznan.put.logic.common.validation.number;

import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

import java.util.function.Predicate;

public class PositiveDoublePropertyValidator extends AbstractPropertyValidator<String> {
    public PositiveDoublePropertyValidator(String field) {
        super(field);
    }

    @Override
    public Predicate<String> getPredicate() {
        return s -> {
            try {
                return Double.parseDouble(s) > 0;
            }
            catch (Exception e) {
                return false;
            }
        };
    }

    @Override
    public String getErrorMessage() {
        return field + " has to be a positive number";
    }
}
