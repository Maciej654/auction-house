package pl.poznan.put.logic.auction.validation;

import pl.poznan.put.logic.common.validation.AbstractNotTruePropertyValidator;

public class PhotosListNotEmptyPropertyValidator extends AbstractNotTruePropertyValidator {
    public PhotosListNotEmptyPropertyValidator() {
        super("Photos list");
    }

    @Override
    public String getErrorMessage() {
        return field + " cannot be empty";
    }
}
