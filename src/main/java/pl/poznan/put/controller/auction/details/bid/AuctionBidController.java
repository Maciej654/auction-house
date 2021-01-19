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
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.number.GreaterThanOrEqualDoublePropertyValidator;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Status;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.time.LocalDateTime;


@Slf4j
public class AuctionBidController extends AbstractValidatedController {
    @FXML
    private TextField priceEntry;

    @FXML
    private Button bidButton;

    @FXML
    private ImageView bidWarning;

    @Setter
    private Runnable afterBidCallback = () -> {};

    private final DoubleProperty currentPriceProperty = new SimpleDoubleProperty();

    @Getter
    private final ObjectProperty<Auction> auctionProperty = new SimpleObjectProperty<>();

    private final EntityManager em = EntityManagerProvider.getEntityManager();

    private ChangeListener<? super String> currentListener;

    private Tooltip currentTooltip;

    protected void uninstallCurrentValidation() {
        if (currentListener != null) priceEntry.textProperty().removeListener(currentListener);
        if (currentTooltip != null) Tooltip.uninstall(bidWarning, currentTooltip);
        currentListener = null;
        currentTooltip = null;
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
        currentTooltip = new Tooltip(message);
        currentTooltip.setPrefWidth(200);
        currentTooltip.setWrapText(true);
        Tooltip.install(bidWarning, currentTooltip);

        currentListener.changed(priceEntry.textProperty(), null, priceEntry.getText());
    }

    @Override
    protected void installValidation() {
        bidButton.disableProperty().bind(bidWarning.visibleProperty());
        currentPriceProperty.addListener((observable, oldValue, newValue) -> installCurrentValidation((Double) newValue));
        auctionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) currentPriceProperty.set(newValue.getPrice());
        });
    }

    @Override
    protected void setupInitialValues() {
        setupTextField(priceEntry);
    }

    protected void refreshCurrentPrice() {
        val auction = auctionProperty.get();
        if (auction != null && em != null) {
            em.refresh(auction);
            Platform.runLater(() -> currentPriceProperty.set(auction.getPrice()));
        }
    }

    @FXML
    public void bidButtonClick() {
        log.info("bid");
//        refreshCurrentPrice();
        val auction = auctionProperty.get();
        if (auction != null && em != null && !bidButton.isDisable()) {
            val transaction = em.getTransaction();
            transaction.begin();
            try {
                val value = Double.parseDouble(priceEntry.getText());
                auction.setPrice(value);
                auction.setStatus(Status.BIDDING);
                em.merge(auction);
                delayAuction();
                insertAuctionLog();
                transaction.commit();
                currentPriceProperty.set(value);
                afterBidCallback.run();
            }
            catch (NumberFormatException ignored) {}
            catch (Exception e) {
                log.error(e.getMessage(), e);
                if (transaction.isActive()) transaction.rollback();
            }
        }
    }

    private void delayAuction() {
        val auction = auctionProperty.get();
        if (auction != null && em != null) {
            StoredProcedureQuery query = em.createStoredProcedureQuery("subprograms.delayAuctionsEnd");
            query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.IN);
            query.setParameter("p_id", auction.getId());
            query.execute();
        }
    }

    private void insertAuctionLog() {
        val auction = auctionProperty.get();
        if (auction != null && em != null) {
            AuctionLog auctionLog = new AuctionLog(auction, LocalDateTime.now(),
                                                   "Auction bid to " + priceEntry.getText() + " PLN",
                                                   CurrentUser.getLoggedInUser());
            em.persist(auctionLog);
        }
    }
}