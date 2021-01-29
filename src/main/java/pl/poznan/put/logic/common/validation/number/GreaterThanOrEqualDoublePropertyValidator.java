package pl.poznan.put.logic.common.validation.number;

import pl.poznan.put.util.converter.DoubleConverterUtils;

public class GreaterThanOrEqualDoublePropertyValidator extends AbstractDoublePropertyValidator {
    private final Double value;

    public GreaterThanOrEqualDoublePropertyValidator(String field, Double value) {
        super(field, v -> v >= value);
        this.value = value;
    }

    @Override
    public String getErrorMessage() {
        return field + " must be at least " + DoubleConverterUtils.toString(value);
    }
}
