package pl.poznan.put.logic.common.validation.alpha;

import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

import java.util.function.Predicate;

public class AlphaPropertyValidator extends AbstractPropertyValidator<String> {
    public AlphaPropertyValidator(String field) {
        super(field);
    }

    @Override
    public Predicate<String> getPredicate() {
        return StringUtils::isAlpha;
    }

    @Override
    public String getErrorMessage() {
        return field + " cannot be empty and must consist of letters only";
    }
}
