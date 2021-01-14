package pl.poznan.put.logic.user.validation;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

public class UserUpdatePasswordValidator extends UserCreatePasswordValidator {
    @Override
    public Predicate<String> getPredicate() {
        return super.getPredicate().or(StringUtils::isEmpty);
    }
}
