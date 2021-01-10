package pl.poznan.put.controller.user.validation;

import org.apache.commons.lang3.StringUtils;

public class UserUpdatePasswordValidator extends UserCreatePasswordValidator {
    @Override
    public boolean test(String s) {
        return StringUtils.isEmpty(s) || super.test(s);
    }
}
