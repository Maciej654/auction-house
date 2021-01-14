package pl.poznan.put.logic.common.validation;

import java.util.function.Predicate;

public class AbstractNotTruePropertyValidator extends AbstractPropertyValidator<Boolean> {
    protected AbstractNotTruePropertyValidator(String field) {
        super(field);
    }

    @Override
    public Predicate<Boolean> getPredicate() {
        return b -> !b;
    }
}
