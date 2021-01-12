package pl.poznan.put.logic.common.validation.alpha;

import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.logic.common.validation.PropertyValidator;

public class AlphaPropertyValidator implements PropertyValidator<String> {
    private final String field;

    public AlphaPropertyValidator(String field) {
        this.field = field;
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
