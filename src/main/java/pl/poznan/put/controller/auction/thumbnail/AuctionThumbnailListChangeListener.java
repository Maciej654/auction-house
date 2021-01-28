package pl.poznan.put.controller.auction.thumbnail;

import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.util.view.loader.ViewLoader;

import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class AuctionThumbnailListChangeListener implements ListChangeListener<Auction> {
    private final Consumer<Auction> thumbnailClickCallback;

    private final Map<Auction, Parent> thumbnailCache;

    @Override
    public void onChanged(Change<? extends Auction> change) {
        while (change.next()) {
            if (change.wasAdded()) change.getAddedSubList().forEach(auction -> {
                val thumbnail = ViewLoader.getParent(AuctionThumbnailController.class, controller -> {
                    controller.setClickCallback(thumbnailClickCallback);
                    controller.getAuctionProperty().set(auction);
                });
                if (thumbnail != null) thumbnailCache.put(auction, thumbnail);
            });
            if (change.wasRemoved()) change.getRemoved().forEach(thumbnailCache::remove);
        }
    }
}
