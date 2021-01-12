package pl.poznan.put.logic.user.validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import pl.poznan.put.logic.common.validation.PropertyValidator;

public class UserEmailValidator implements PropertyValidator<String> {
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
