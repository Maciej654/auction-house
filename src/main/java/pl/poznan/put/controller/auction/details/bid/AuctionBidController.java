package pl.poznan.put.controller.auction.details.bid;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.controller.auction.crud.update.task.AuctionBidUpdateTask;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.number.GreaterThanOrEqualDoublePropertyValidator;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.util.callback.Callbacks;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;
import pl.poznan.put.util.task.ProjectTaskUtils;

import javax.persistence.EntityManager;
import java.util.Timer;


@Slf4j
public class AuctionBidController extends AbstractValidatedController {
    @FXML
    private TextField priceEntry;

    @FXML
    private Button bidButton;

    @FXML
    private ImageView bidWarning;

    @Setter
    private Runnable afterBidCallback = Callbacks::noop;

    private final DoubleProperty currentPriceProperty = new SimpleDoubleProperty();

    @Getter
    private final ObjectProperty<Auction> auctionProperty = new SimpleObjectProperty<>();

    private final EntityManager em = EntityManagerProvider.getEntityManager();

    private ChangeListener<? super String> currentListener;

    private Tooltip warningTooltip;

    private Tooltip bidTooltip;

    protected void uninstallCurrentValidation() {
        if (currentListener != null) priceEntry.textProperty().removeListener(currentListener);
        if (warningTooltip != null) Tooltip.uninstall(bidWarning, warningTooltip);
        currentListener = null;
        warningTooltip = null;
    }

    protected void installCurrentValidation(Double currentPrice) {
        uninstallCurrentValidation();

        val validator = new GreaterThanOrEqualDoublePropertyValidator("New bidding", currentPrice + 1);

        val predicate = validator.getPredicate();
        currentListener = (observable, oldValue, newValue) -> {
            val valid = predicate.test(newValue);
            bidWarning.setVisible(!valid);
        };
        priceEntry.textProperty().addListener(currentListener);

        val message = validator.getErrorMessage();
        warningTooltip = new Tooltip(message);
        warningTooltip.setPrefWidth(200);
        warningTooltip.setWrapText(true);
        Tooltip.install(bidWarning, warningTooltip);

        currentListener.changed(priceEntry.textProperty(), null, priceEntry.getText());
    }

    private void unbindBidButton() {
        bidButton.disableProperty().unbind();
        bidButton.setDisable(true);
        if (bidTooltip != null) Tooltip.uninstall(bidButton, bidTooltip);
        bidTooltip = new Tooltip("Task running in background");
    }

    private void bindBidButton() {
        bidButton.disableProperty().bind(bidWarning.visibleProperty());
        if (bidTooltip != null) Tooltip.uninstall(bidButton, bidTooltip);
        bidTooltip = new Tooltip("Place your bid");
        Tooltip.install(bidButton, bidTooltip);
    }

    @Override
    protected void installValidation() {
        currentPriceProperty.addListener((observable, oldValue, newValue) -> installCurrentValidation((Double) newValue));
    }

    @Override
    protected void initialize() {
        super.initialize();
        bindBidButton();
        auctionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) currentPriceProperty.set(newValue.getPrice());
        });
    }

    @Override
    protected void setupInitialValues() {
        setupTextField(priceEntry);
    }

    private void refreshCurrentPrice() {
        val auction = auctionProperty.get();
        if (auction != null && em != null) {
            em.refresh(auction);
            Platform.runLater(() -> currentPriceProperty.set(auction.getPrice()));
        }
    }

    private final Timer timer = new Timer();

    @FXML
    public void bidButtonClick() {
        log.info("bid");
        val auction = auctionProperty.get();
        if (auction != null) try {
            val price = Double.parseDouble(priceEntry.getText());
            unbindBidButton();
            timer.schedule(
                    new AuctionBidUpdateTask(
                            auction,
                            CurrentUser.getLoggedInUser(),
                            price,
                            () -> Platform.runLater(() -> {
                                currentPriceProperty.set(price);
                                afterBidCallback.run();
                            }),
                            Callbacks::noop,
                            () -> Platform.runLater(this::bindBidButton)
                    ),
                    ProjectTaskUtils.IMMEDIATE
            );
        }
        catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
    }
}