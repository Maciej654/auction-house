package pl.poznan.put.logic.common.validation.empty;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

public class NotBlankPropertyValidator extends AbstractNotEmptyPropertyValidator<String> {
    public NotBlankPropertyValidator(String field) {
        super(field);
    }

    @Override
    public Predicate<String> getPredicate() {
        return StringUtils::isNotBlank;
    }
}
