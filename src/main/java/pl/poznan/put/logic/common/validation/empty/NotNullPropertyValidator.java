package pl.poznan.put.logic.common.validation.empty;

import java.util.Objects;
import java.util.function.Predicate;

public class NotNullPropertyValidator<T> extends AbstractNotEmptyPropertyValidator<T> {
    public NotNullPropertyValidator(String field) {
        super(field);
    }

    @Override
    public Predicate<T> getPredicate() {
        return Objects::nonNull;
    }
}
