package pl.poznan.put.controller.auction.details.management;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.ad.Ad;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Status;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.util.converter.EnumConverterUtils;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Slf4j
public class AuctionManagementController {
    @FXML
    private Label feedbackLabel;

    @FXML
    private Label auctionStatusLabel;

    @FXML
    private Label errorLabel;

    @Getter
    private final ObjectProperty<Auction> auctionProperty = new SimpleObjectProperty<>();

    private final EntityManager em = EntityManagerProvider.getEntityManager();

    private void customizeView(Auction auction) {
        auctionStatusLabel.setText(EnumConverterUtils.toString(auction.getStatus()));
    }

    @FXML
    private void initialize() {
        auctionProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) customizeView(newValue);
        });
    }

    @FXML
    private void finalizeButtonClick() {
        log.info("cancel");

        val auction = auctionProperty.get();
        if (auction == null) {
            log.warn("auction is null");
            return;
        }

        if (!auction.isActive()) {
            errorLabel.setText("Auction cannot be cancelled");
            return;
        }

        if (em == null) {
            log.warn("entity manager is null");
            return;
        }

        auction.setStatus(Status.CANCELLED);
        val auctionCancelledLog = new AuctionLog(
                auction,
                LocalDateTime.now(),
                "Auction cancelled",
                CurrentUser.getLoggedInUser()
        );
        auction.addLog(auctionCancelledLog);

        mergeAuction(auction);
    }

    @FXML
    private void advertiseButtonClick() {
        log.info("advertise");

        val auction = auctionProperty.get();
        if (auction == null) {
            log.warn("auction is null");
            return;
        }

        if (em == null) {
            log.warn("entity manager is null");
            return;
        }

        var ad = auction.getAd();
        if (ad != null) {
            errorLabel.setText("Auction is already being advertised");
            return;
        }

        ad = new Ad(auction.getId(), auction, "Best " + auction.getItemName() + " ever!");
        auction.setAd(ad);
        val transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(ad);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            log.error(e.getMessage(), e);
            errorLabel.setText(e.getMessage());
            return;
        }

        val auctionAdCreatedLog = new AuctionLog(
                auction,
                LocalDateTime.now(),
                "Ad for item '" + auction.getItemName() + "' created",
                CurrentUser.getLoggedInUser()
        );
        auction.addLog(auctionAdCreatedLog);

        mergeAuction(auction);
    }

    private void mergeAuction(Auction auction) {
        feedbackLabel.setText(StringUtils.EMPTY);
        val transaction = em.getTransaction();
        transaction.begin();
        try {
            em.merge(auction);
            transaction.commit();
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            if (transaction.isActive()) transaction.rollback();
            errorLabel.setText("Something went wrong during completing your request");
            return;
        }

        errorLabel.setText(StringUtils.EMPTY);
        feedbackLabel.setText("Success");
        log.info("auction '{}' merged successfully", auction.getAuctionName());
        customizeView(auction);
    }

    @FXML
    private void finishButtonClick() {
        log.info("finish");

        val auction = auctionProperty.get();
        if (auction == null) {
            log.warn("auction is null");
            return;
        }

        if (em == null) {
            log.warn("entity manager is null");
            return;
        }

        if (!auction.isActive()) {
            errorLabel.setText("Auction cannot be finished");
            return;
        }

        auction.setStatus(Status.FINISHED);
        val auctionFinishedLog = new AuctionLog(
                auction,
                LocalDateTime.now(),
                "Auction finished",
                CurrentUser.getLoggedInUser()
        );
        auction.addLog(auctionFinishedLog);

        mergeAuction(auction);
    }
}
