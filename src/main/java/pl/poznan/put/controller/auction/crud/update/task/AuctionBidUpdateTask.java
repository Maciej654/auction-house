package pl.poznan.put.controller.auction.crud.update.task;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.Auction.Status;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.function.Consumer;

@Slf4j
public class AuctionBidUpdateTask extends TimerTask {
    private static final String LOG_FORMAT = "Auction bid to %.2f PLN by %s";

    private final Auction auction;

    private final User bidder;

    private final double price;

    private final Runnable onSuccessCallback;

    private final Consumer<Exception> onFailureCallback;

    private final Runnable cleanup;

    public AuctionBidUpdateTask(Auction auction, User bidder, double price, Runnable onSuccessCallback,
                                Consumer<Exception> onFailureCallback, Runnable cleanup) {
        this.auction = auction;
        this.bidder = bidder;
        this.price = price;
        this.onSuccessCallback = onSuccessCallback;
        this.onFailureCallback = onFailureCallback;
        this.cleanup = cleanup;
    }

    private void delayAuction(Auction auction, EntityManager em) {
        val query = em.createStoredProcedureQuery("subprograms.delayAuctionsEnd");
        query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.IN);
        query.setParameter("p_id", auction.getId());
        query.execute();
    }

    @Override
    public void run() {
        val em          = EntityManagerProvider.getEntityManager();
        val transaction = em.getTransaction();
        try {
            auction.setPrice(price);
            auction.setStatus(Status.BIDDING);
            val description = String.format(LOG_FORMAT, price, bidder.getFirstName());
            AuctionLog auctionLog = new AuctionLog(
                    auction,
                    LocalDateTime.now(),
                    description,
                    bidder
            );
            auction.addLog(auctionLog);
            delayAuction(auction, em);
            em.merge(auction);
            transaction.commit();
            onSuccessCallback.run();
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            if (transaction.isActive()) transaction.rollback();
            onFailureCallback.accept(e);
        }
        finally {
            cleanup.run();
        }
    }
}
