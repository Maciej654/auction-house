package pl.poznan.put.controller.auction.crud.create.specifics.book;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.alpha.AlphaPropertyValidator;
import pl.poznan.put.logic.common.validation.empty.NotNullPropertyValidator;
import pl.poznan.put.model.auction.book.Book;
import pl.poznan.put.model.auction.book.Book.Cover;
import pl.poznan.put.util.converter.EnumConverter;
import pl.poznan.put.util.validation.Validation;

public class AuctionCreateBookController extends AbstractValidatedController {
    @Getter
    private final Book.BookBuilder<?, ?> bookBuilder = Book.builder();

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
                new AlphaPropertyValidator("Genre"),
                genreValid,
                genreWarning
        );

        // cover
        Validation.install(
                coverChoiceBox.valueProperty(),
                new NotNullPropertyValidator<>("Cover"),
                coverValid,
                coverWarning
        );

        // author
        Validation.install(
                authorTextField.textProperty(),
                new AlphaPropertyValidator("Author"),
                authorValid,
                authorWarning
        );

        val valid = genreValid
                .and(coverValid)
                .and(authorValid);

        informationValid.bind(valid);
    }

    @Override
    protected void setupInitialValues() {
        genreTextField.setText(null);

        val items = FXCollections.observableArrayList(Cover.values());
        coverChoiceBox.setConverter(new EnumConverter<>(Cover.class));
        coverChoiceBox.setItems(items);
        coverChoiceBox.setValue(null);

        authorTextField.setText(null);
    }
}
