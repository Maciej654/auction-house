package pl.poznan.put.logic.user.validation;

import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.logic.common.validation.PropertyValidator;

public class UserCreatePasswordValidator implements PropertyValidator<String> {
    private boolean containsLowerCaseLetter(String s) {
        return s.matches(".*[a-z].*");
    }

    private boolean containsUpperCaseLetter(String s) {
        return s.matches(".*[A-Z].*");
    }

    private boolean containsDigit(String s) {
        return s.matches(".*[0-9].*");
    }

    private boolean isLongEnough(String s) {
        return s.length() >= 8;
    }

    private boolean containsOnlyAlphaNumericCharacters(String s) {
        return s.matches("[a-zA-Z0-9]*");
    }

    @Override
    public boolean test(String s) {
        return StringUtils.isNotBlank(s) &&
               isLongEnough(s) &&
               containsOnlyAlphaNumericCharacters(s) &&
               containsLowerCaseLetter(s) &&
               containsUpperCaseLetter(s) &&
               containsDigit(s);
    }

    @Override
    public String getErrorMessage() {
        return "Password must be at least 8 characters long," +
               " contain a lower case letter," +
               " an upper case letter," +
               " and a digit";
    }
}
