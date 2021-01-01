package pl.poznan.put.controller.user.validation;

import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

public class UserConfirmPasswordValidator implements UserPropertyValidator<String> {
    private final StringProperty reference;

    public UserConfirmPasswordValidator(StringProperty reference) {
        this.reference = reference;
    }

    @Override
    public String getErrorMessage() {
        return "Passwords don't match";
    }

    @Override
    public boolean test(String s) {
        return StringUtils.equals(reference.getValue(), s);
    }
}
