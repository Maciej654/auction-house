package pl.poznan.put.controller.auction.crud.create.specifics.phone;

import lombok.Getter;
import pl.poznan.put.model.auction.phone.Phone;

public class AuctionCreatePhoneController {
    @Getter
    private final Phone.PhoneBuilder<?, ?> phoneBuilder = Phone.builder();
}
