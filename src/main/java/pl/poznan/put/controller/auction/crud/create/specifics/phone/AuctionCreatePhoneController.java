package pl.poznan.put.controller.auction.crud.create.specifics.phone;

import lombok.Getter;
import pl.poznan.put.controller.auction.crud.create.AbstractValidatedController;
import pl.poznan.put.model.auction.phone.Phone;

public class AuctionCreatePhoneController extends AbstractValidatedController {
    @Getter
    private final Phone.PhoneBuilder<?, ?> phoneBuilder = Phone.builder();

    @Override
    protected void installValidation() {

    }

    @Override
    protected void setupInitialValues() {

    }
}
