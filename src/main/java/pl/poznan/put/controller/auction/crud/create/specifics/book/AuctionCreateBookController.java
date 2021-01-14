package pl.poznan.put.controller.auction.crud.create.specifics.book;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.specifics.AuctionBuilderController;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.alpha.AlphaPropertyValidator;
import pl.poznan.put.logic.common.validation.empty.NotNullPropertyValidator;
import pl.poznan.put.model.auction.Auction.AuctionBuilder;
import pl.poznan.put.model.auction.book.Book;
import pl.poznan.put.model.auction.book.Book.Cover;
import pl.poznan.put.util.validation.Validation;

public class AuctionCreateBookController extends AbstractValidatedController implements AuctionBuilderController {
    @FXML
    private ImageView genreWarning;

    @FXML
    private TextField genreTextField;

    private final BooleanProperty genreValid = new SimpleBooleanProperty();

    @FXML
    private ImageView coverWarning;

    @FXML
    private ChoiceBox<Book.Cover> coverChoiceBox;

    private final BooleanProperty coverValid = new SimpleBooleanProperty();

    @FXML
    private ImageView authorWarning;

    @FXML
    private TextField authorTextField;

    private final BooleanProperty authorValid = new SimpleBooleanProperty();

    @Override
    protected void installValidation() {
        // genre
        Validation.install(
                genreTextField.textProperty(),
                genreValid,
                genreWarning,
                new AlphaPropertyValidator("Genre")
        );

        // cover
        Validation.install(
                coverChoiceBox.valueProperty(),
                coverValid,
                coverWarning,
                new NotNullPropertyValidator<>("Cover")
        );

        // author
        Validation.install(
                authorTextField.textProperty(),
                authorValid,
                authorWarning,
                new AlphaPropertyValidator("Author")
        );

        val valid = genreValid
                .and(coverValid)
                .and(authorValid);

        informationValid.bind(valid);
    }

    @Override
    protected void setupInitialValues() {
        setupTextField(genreTextField);
        setupTextField(authorTextField);
        setupChoiceBox(coverChoiceBox, Cover.class);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AuctionBuilder getAuctionBuilder() {
        return Book.builder()
                   .author(authorTextField.getText())
                   .cover(coverChoiceBox.getValue())
                   .genre(genreTextField.getText());
    }
}
