package pl.poznan.put.controller.auction.crud.create;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import pl.poznan.put.controller.auction.crud.create.specifics.AuctionCreateSpecificsController;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Status;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.date.ProjectDateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.function.Consumer;

public class AuctionCreateController extends AbstractValidatedController {
    @FXML
    private HTMLEditor descriptionEditor;

    @FXML
    private TextField initialPriceTextField;

    private final BooleanProperty initialPriceValid = new SimpleBooleanProperty();

    @FXML
    private TextField itemNameTextField;

    private final BooleanProperty itemNameValid = new SimpleBooleanProperty();

    @FXML
    private TextField auctionNameTextField;

    private final BooleanProperty auctionNameValid = new SimpleBooleanProperty();

    @FXML
    private Label endLabel;

    @FXML
    private Label userLabel;

    @FXML
    private AuctionCreateSpecificsController auctionCreateSpecificsController;

    @Getter
    private final ObjectProperty<User> userProperty = new SimpleObjectProperty<>();

    @Override
    @FXML
    protected void initialize() {
        super.initialize();

        userProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) userLabel.setText(newValue.getFullName());
        });

        val endDate = DateUtils.addDays(new Date(), 30);
        val endText = DateFormatUtils.format(endDate, ProjectDateUtils.PATTERN);
        endLabel.setText(endText);
    }

    @Setter
    private Consumer<Auction> createAuctionCallback = auction -> {};

    @FXML
    private void createButtonClick() throws ParseException {
        val auctionName     = auctionNameTextField.getText();
        val itemName        = itemNameTextField.getText();
        val itemDescription = descriptionEditor.getHtmlText();
        val creationDate    = new Date();
        val endDate         = DateUtils.parseDate(endLabel.getText(), ProjectDateUtils.PATTERN);
        val initialPrice    = Double.parseDouble(initialPriceTextField.getText());
        val status          = Status.CREATED;
        val discriminator   = "???";
    }

    @Override
    protected void installValidation() {
        // TODO setup validation of initial price
        // TODO setup validation of item name
        // TODO setup validation of auction name

        val valid = initialPriceValid
                .and(itemNameValid)
                .and(auctionNameValid)
                .and(auctionCreateSpecificsController.informationValid);

        informationValid.bind(valid);
    }

    @Override
    protected void setupInitialValues() {}
}
