package pl.poznan.put.logic.common.validation.alpha;

import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

public class AlphaPropertyValidator extends AbstractPropertyValidator<String> {
    public AlphaPropertyValidator(String field) {
        super(field);
    }

    @Override
    public boolean test(String s) {
        return StringUtils.isAlpha(s);
    }

    @Override
    public String getErrorMessage() {
        return field + " cannot be empty and must consist of letters only";
    }
}
