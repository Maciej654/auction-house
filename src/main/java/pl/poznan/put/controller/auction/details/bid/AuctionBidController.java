package pl.poznan.put.controller.auction.details.bid;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.auction.details.AuctionDetailsController;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.popup.PopUpWindow;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Slf4j
public class AuctionBidController {
    @FXML
    public TextField priceEntry;
    @FXML
    public Button bidButton;
    @Setter
    private Auction auction;
    @Setter
    AuctionDetailsController auctionDetailsController;

    private final EntityManager entityManager = EntityManagerProvider.getEntityManager();

    @FXML
    private void initialize() {
        log.info("initialize");
    }

    @FXML
    public void bid(ActionEvent actionEvent) {
        Objects.requireNonNull(entityManager).refresh(auction); //to make sure we have current version on auction

        String priceText = priceEntry.getText();
        if (!validateInput(priceText)) {
            auctionDetailsController.updateLabels();
            return;
        }
        double price = Double.parseDouble(priceText);
        auction.setPrice(price);
        auction.setStatus(Auction.Status.BIDDING);

        EntityTransaction transaction = Objects.requireNonNull(entityManager).getTransaction();

        transaction.begin();
        entityManager.merge(auction);
        delayAuction();
        insertAuctionLog();
        transaction.commit();
        auctionDetailsController.updateLabels();
        auctionDetailsController.updateHistory();

    }

    private boolean validateInput(String priceText){
        double price;
        try{
            price = Double.parseDouble(priceText);
        }catch(NumberFormatException e){
            PopUpWindow.display("Incorrect format");
            return false;
        }
       if (price <= auction.getPrice()){
           PopUpWindow.display("you must pay more than the current price");
           return false;
       }
       if(auction.getStatus().equals(Auction.Status.FINISHED) || auction.getStatus().equals(Auction.Status.CANCELLED)){
           PopUpWindow.display("Auction is no longer available");
           return false;
       }
       return true;
    }

    private void delayAuction(){
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("subprograms.delayAuctionsEnd");
        query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.IN);
        query.setParameter("p_id", auction.getId());
        query.execute();
    }

    private void insertAuctionLog(){
        //toDo include the user
        AuctionLog auctionLog = new AuctionLog(auction, LocalDateTime.now(), "auction bid to " + priceEntry.getText() + " PLN", null);
        Objects.requireNonNull(entityManager).persist(auctionLog);

    }
}