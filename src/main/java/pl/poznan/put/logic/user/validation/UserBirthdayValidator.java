package pl.poznan.put.logic.user.validation;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.logic.common.validation.empty.NotNullPropertyValidator;

import java.time.LocalDate;
import java.util.function.Predicate;

public class UserBirthdayValidator extends NotNullPropertyValidator<LocalDate> {
    public UserBirthdayValidator() {
        super(StringUtils.EMPTY);
    }

    @Override
    public Predicate<LocalDate> getPredicate() {
        return super.getPredicate().and(date -> {
            val today = LocalDate.now();
            return date.plusYears(18).isBefore(today);
        });
    }

    @Override
    public String getErrorMessage() {
        return "User has to be at least 18 years old";
    }
}
