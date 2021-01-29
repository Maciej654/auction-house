package pl.poznan.put.controller.auction.crud.create;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.web.HTMLEditor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.controller.auction.crud.create.photos.AuctionCreatePhotosController;
import pl.poznan.put.controller.auction.crud.create.specifics.AuctionCreateSpecificsController;
import pl.poznan.put.controller.auction.crud.create.task.AuctionCreateTask;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.empty.NotBlankPropertyValidator;
import pl.poznan.put.logic.common.validation.number.PositiveDoublePropertyValidator;
import pl.poznan.put.logic.common.validation.number.PricePropertyValidator;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Status;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.callback.Callbacks;
import pl.poznan.put.util.converter.DateConverterUtils;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;
import pl.poznan.put.util.task.ProjectTaskUtils;
import pl.poznan.put.util.validation.Validation;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;
import java.util.function.Consumer;

@Slf4j
public class AuctionCreateController extends AbstractValidatedController {
    @FXML
    private Pane spacePane;

    @FXML
    private Label errorLabel;

    @FXML
    private HTMLEditor descriptionEditor;

    @FXML
    private TextField initialPriceTextField;

    @FXML
    private ImageView initialPriceWarning;

    private final BooleanProperty initialPriceValid = new SimpleBooleanProperty();

    @FXML
    private TextField itemNameTextField;

    @FXML
    private ImageView itemNameWarning;

    private final BooleanProperty itemNameValid = new SimpleBooleanProperty();

    @FXML
    private TextField auctionNameTextField;

    @FXML
    private ImageView auctionNameWarning;

    private final BooleanProperty auctionNameValid = new SimpleBooleanProperty();

    @FXML
    private Label endLabel;

    @FXML
    private Label userLabel;

    @FXML
    private AuctionCreateSpecificsController auctionCreateSpecificsController;

    @FXML
    private AuctionCreatePhotosController auctionCreatePhotosController;

    @FXML
    private Button createButton;

    @Getter
    private final ObjectProperty<User> userProperty = new SimpleObjectProperty<>();

    @Setter
    private Runnable backCallback = Callbacks::noop;

    @FXML
    private void backButtonClick() {
        log.info("back");

        backCallback.run();
    }

    @Override
    @FXML
    protected void initialize() {
        super.initialize();
        log.info("initialize");

        HBox.setHgrow(spacePane, Priority.ALWAYS);

        userProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) userLabel.setText(newValue.getFullName());
        });

        val endLocalDate     = LocalDate.now().plusDays(30);
        val endLocalTime     = LocalTime.now();
        val endLocalDateTime = LocalDateTime.of(endLocalDate, endLocalTime);
        val endText          = DateConverterUtils.toString(endLocalDateTime);
        endLabel.setText(endText);
    }

    private final EntityManager em = EntityManagerProvider.getEntityManager();

    private final Timer timer = new Timer();

    @Setter
    private Consumer<Auction> createAuctionCallback = Callbacks::noop;

    @FXML
    private void createButtonClick() {
        if (em == null) {
            log.error("entity manager is null");
            return;
        }

        val creationLocalDate     = LocalDate.now();
        val creationLocalTime     = LocalTime.now();
        val creationLocalDateTime = LocalDateTime.of(creationLocalDate, creationLocalTime);

        val endLocalDate     = creationLocalDate.plusDays(30);
        val endLocalDateTime = LocalDateTime.of(endLocalDate, creationLocalTime);

        val initialPrice = Double.parseDouble(initialPriceTextField.getText());

        val auction = auctionCreateSpecificsController
                .getAuctionBuilder()
                .auctionName(auctionNameTextField.getText())
                .itemName(itemNameTextField.getText())
                .price(initialPrice)
                .itemDescription(descriptionEditor.getHtmlText())
                .creationDate(creationLocalDateTime)
                .endDate(endLocalDateTime)
                .status(Status.CREATED)
                .seller(userProperty.get())
                .build();

        val pictures = auctionCreatePhotosController.getPictures(auction);
        auction.setPictures(pictures);

        timer.schedule(
                new AuctionCreateTask(auction, createAuctionCallback, this::setErrorLabel),
                ProjectTaskUtils.IMMEDIATE
        );
    }

    private void setErrorLabel(Exception e) {
        Platform.runLater(() -> errorLabel.setText(e.getMessage()));
    }

    @Override
    protected void installValidation() {
        // initial price
        Validation.install(
                initialPriceTextField.textProperty(),
                initialPriceValid,
                initialPriceWarning,
                new PricePropertyValidator(new PositiveDoublePropertyValidator("Initial price"))
        );

        // item name
        Validation.install(
                itemNameTextField.textProperty(),
                itemNameValid,
                itemNameWarning,
                new NotBlankPropertyValidator("Item name")
        );

        // auction name
        Validation.install(
                auctionNameTextField.textProperty(),
                auctionNameValid,
                auctionNameWarning,
                new NotBlankPropertyValidator("Auction name")
        );

        val valid = initialPriceValid
                .and(itemNameValid)
                .and(auctionNameValid)
                .and(auctionCreateSpecificsController.getInformationValid())
                .and(auctionCreatePhotosController.getInformationValid());

        createButton.disableProperty().bind(valid.not());
    }

    @Override
    protected void setupInitialValues() {
        setupTextField(initialPriceTextField);
        setupTextField(itemNameTextField);
        setupTextField(auctionNameTextField);
    }
}
