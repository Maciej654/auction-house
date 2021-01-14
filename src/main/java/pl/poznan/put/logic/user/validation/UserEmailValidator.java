package pl.poznan.put.logic.user.validation;

import lombok.val;
import org.apache.commons.validator.routines.EmailValidator;
import pl.poznan.put.logic.common.validation.AbstractPropertyValidator;

import java.util.function.Predicate;

public class UserEmailValidator extends AbstractPropertyValidator<String> {

    public UserEmailValidator() {
        super("Email");
    }

    @Override
    public Predicate<String> getPredicate() {
        val emailValidator = EmailValidator.getInstance();
        return emailValidator::isValid;
    }
}
