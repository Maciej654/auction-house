package pl.poznan.put.logic.common.validation.empty;

import org.apache.commons.lang3.StringUtils;

public class NotBlankPropertyValidator extends AbstractNotEmptyPropertyValidator<String> {
    public NotBlankPropertyValidator(String field) {
        super(field);
    }

    @Override
    public boolean test(String s) {
        return StringUtils.isNotBlank(s);
    }
}
