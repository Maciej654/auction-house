package pl.poznan.put.logic.common.validation.empty;

public class NotNullPropertyValidator<T> extends AbstractNotEmptyPropertyValidator<T> {
    public NotNullPropertyValidator(String field) {
        super(field);
    }

    @Override
    public boolean test(T t) {
        return t != null;
    }
}
