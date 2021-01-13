package pl.poznan.put.controller.auction.crud.create.specifics;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.Getter;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.alpha.AlphaPropertyValidator;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Default;
import pl.poznan.put.util.validation.Validation;

public class AuctionCreateDefaultController extends AbstractValidatedController {
    @SuppressWarnings("rawtypes")
    @Getter
    private final Auction.AuctionBuilder auctionBuilder = Default.builder();

    @FXML
    private ImageView categoryWarning;

    @FXML
    private TextField categoryTextField;

    @Override
    protected void installValidation() {
        Validation.install(
                categoryTextField.textProperty(),
                new AlphaPropertyValidator("Category"),
                informationValid,
                categoryWarning
        );
    }

    @Override
    protected void setupInitialValues() {
        setupTextField(categoryTextField);
    }
}
