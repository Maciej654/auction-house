package pl.poznan.put.controller.user.validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class UserEmailValidator implements UserPropertyValidator<String> {
    private final EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public boolean test(String s) {
        return StringUtils.isNotBlank(s) && emailValidator.isValid(s);
    }

    @Override
    public String getErrorMessage() {
        return "Email is not valid";
    }
}
