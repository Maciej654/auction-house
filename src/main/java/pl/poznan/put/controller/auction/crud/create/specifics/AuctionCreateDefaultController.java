package pl.poznan.put.controller.auction.crud.create.specifics;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.alpha.AlphaPropertyValidator;
import pl.poznan.put.model.auction.Auction.AuctionBuilder;
import pl.poznan.put.model.auction.Auction.Type;
import pl.poznan.put.model.auction.Default;
import pl.poznan.put.util.validation.Validation;

public class AuctionCreateDefaultController extends AbstractValidatedController implements AuctionBuilderController {
    @FXML
    private ImageView categoryWarning;

    @FXML
    private TextField categoryTextField;

    @Override
    protected void installValidation() {
        Validation.install(
                categoryTextField.textProperty(),
                informationValid,
                categoryWarning,
                new AlphaPropertyValidator("Category")
        );
    }

    @Override
    protected void setupInitialValues() {
        setupTextField(categoryTextField);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AuctionBuilder getAuctionBuilder() {
        return Default.builder()
                      .category(categoryTextField.getText())
                      .type(Type.DEFAULT);
    }
}
