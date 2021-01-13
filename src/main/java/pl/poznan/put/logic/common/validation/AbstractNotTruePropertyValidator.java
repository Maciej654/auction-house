package pl.poznan.put.logic.common.validation;

public class AbstractNotTruePropertyValidator extends AbstractPropertyValidator<Boolean> {
    protected AbstractNotTruePropertyValidator(String field) {
        super(field);
    }

    @Override
    public boolean test(Boolean value) {
        return !value;
    }
}
