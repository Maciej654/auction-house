package pl.poznan.put.controller.auction.crud.create.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

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
        val em          = EntityManagerProvider.getEntityManager();
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
