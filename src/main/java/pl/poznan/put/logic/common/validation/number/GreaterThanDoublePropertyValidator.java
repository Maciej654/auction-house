package pl.poznan.put.logic.common.validation.number;

public class GreaterThanDoublePropertyValidator extends AbstractDoublePropertyValidator {
    private final Double value;

    public GreaterThanDoublePropertyValidator(String field, Double value) {
        super(field, v -> v > value);
        this.value = value;
    }

    @Override
    public String getErrorMessage() {
        return field + " must be greater than " + value;
    }
}
