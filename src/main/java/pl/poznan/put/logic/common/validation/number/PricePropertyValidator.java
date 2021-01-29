package pl.poznan.put.logic.common.validation.number;

import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class PricePropertyValidator extends AbstractPropertyValidator<String> {
    private final Pattern regex = Pattern.compile("-?[0-9]+([,.][0-9]{1,2})?");

    private final AbstractDoublePropertyValidator abstractDoublePropertyValidator;

    public PricePropertyValidator(AbstractDoublePropertyValidator abstractDoublePropertyValidator) {
        super("Price");
        this.abstractDoublePropertyValidator = abstractDoublePropertyValidator;
    }

    @Override
    public Predicate<String> getPredicate() {
        return abstractDoublePropertyValidator.getPredicate().and(s -> regex.matcher(s).matches());
    }

    @Override
    public String getErrorMessage() {
        return abstractDoublePropertyValidator.getErrorMessage() + " and can have at most 2 decimal places";
    }
}
