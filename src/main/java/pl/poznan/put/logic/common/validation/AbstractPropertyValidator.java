package pl.poznan.put.logic.common.validation;

public abstract class AbstractPropertyValidator<T> implements PropertyValidator<T> {
    protected final String field;

    protected AbstractPropertyValidator(String field) {
        this.field = field;
    }

    @Override
    public String getErrorMessage() {
        return field + " is not valid";
    }
}
