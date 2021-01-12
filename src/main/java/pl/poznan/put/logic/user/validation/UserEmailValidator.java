package pl.poznan.put.logic.user.validation;

import org.apache.commons.validator.routines.EmailValidator;
import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

public class UserEmailValidator extends AbstractPropertyValidator<String> {
    private final EmailValidator emailValidator = EmailValidator.getInstance();

    public UserEmailValidator() {
        super("Email");
    }

    @Override
    public boolean test(String s) {
        return emailValidator.isValid(s);
    }
}
