package pl.poznan.put.controller.user.validation;

import java.util.function.Predicate;

public interface UserPropertyValidator<T> extends Predicate<T> {
    String getErrorMessage();
}
