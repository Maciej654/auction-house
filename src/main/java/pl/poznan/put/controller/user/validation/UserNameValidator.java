package pl.poznan.put.controller.user.validation;

import org.apache.commons.lang3.StringUtils;

public class UserNameValidator implements UserPropertyValidator<String> {
    private final String field;

    public UserNameValidator(String field) {
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
