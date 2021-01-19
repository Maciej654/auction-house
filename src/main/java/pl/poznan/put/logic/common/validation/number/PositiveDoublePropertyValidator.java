package pl.poznan.put.logic.common.validation.number;

public class PositiveDoublePropertyValidator extends GreaterThanDoublePropertyValidator {
    public PositiveDoublePropertyValidator(String field) {
        super(field, 0.0);
    }
}
