package pl.poznan.put.logic.user.validation;

import lombok.val;
import pl.poznan.put.logic.common.validation.PropertyValidator;

import java.time.LocalDate;
import java.util.Objects;

public class UserBirthdayValidator implements PropertyValidator<LocalDate> {
    @Override
    public boolean test(LocalDate localDate) {
        val today = LocalDate.now();
        return Objects.nonNull(localDate) && localDate.plusYears(18).isBefore(today);
    }

    @Override
    public String getErrorMessage() {
        return "User has to be at least 18 years old";
    }
}
