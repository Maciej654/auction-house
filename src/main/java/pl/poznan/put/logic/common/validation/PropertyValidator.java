package pl.poznan.put.logic.common.validation;

import java.util.function.Predicate;

public interface PropertyValidator<T> {
    String getErrorMessage();

    Predicate<T> getPredicate();
}
