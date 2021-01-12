package pl.poznan.put.logic.common.validation.empty;

import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

public abstract class AbstractNotEmptyPropertyValidator<T> extends AbstractPropertyValidator<T> {
    protected AbstractNotEmptyPropertyValidator(String field) {
        super(field);
    }

    @Override
    public String getErrorMessage() {
        return field + " cannot be empty";
    }
}
