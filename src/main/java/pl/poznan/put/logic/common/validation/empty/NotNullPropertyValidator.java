package pl.poznan.put.logic.common.validation.empty;

import pl.poznan.put.logic.common.validation.PropertyValidator;

public class NotNullPropertyValidator<T> implements PropertyValidator<T> {
    private final String field;

    public NotNullPropertyValidator(String field) {
        this.field = field;
    }

    @Override
    public String getErrorMessage() {
        return field + " cannot be empty";
    }

    @Override
    public boolean test(T t) {
        return t != null;
    }
}
