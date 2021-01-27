package pl.poznan.put.controller.auction.crud.create.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.logic.auction.exception.AuctionAlreadyExistsException;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.NoResultException;
import java.util.TimerTask;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class AuctionCreateTask extends TimerTask {
    private final Auction auction;

    private final Consumer<Auction> onSuccessCallback;

    private final Consumer<Exception> onFailureCallback;

    @Override
    public void run() {
        val em    = EntityManagerProvider.getEntityManager();
        val query = em.createNamedQuery(Auction.QUERY_FIND_BY_UNIQUE_KEY, Auction.class);
        query.setParameter(Auction.PARAM_AUCTION_NAME, auction.getAuctionName());
        query.setParameter(Auction.PARAM_ITEM_NAME, auction.getItemName());
        query.setParameter(Auction.PARAM_SELLER, auction.getSeller());
        try {
            val other = query.getSingleResult();
            if (other != null) {
                onFailureCallback.accept(
                        new AuctionAlreadyExistsException(auction.getAuctionName(), auction.getItemName())
                );
                return;
            }
        }
        catch (NoResultException ignored) {
            // do nothing
        }

        val auctionCreated = new AuctionLog(
                auction,
                auction.getCreationDate(),
                "Auction created",
                auction.getSeller()
        );
        auction.addLog(auctionCreated);

        val transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(auction);
            transaction.commit();
            onSuccessCallback.accept(auction);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            if (transaction.isActive()) transaction.rollback();
            onFailureCallback.accept(e);
        }
    }
}
