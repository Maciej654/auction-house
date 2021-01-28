package pl.poznan.put.controller.auction.thumbnail;

import javafx.collections.MapChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.poznan.put.model.auction.Auction;

import java.util.List;

@RequiredArgsConstructor
public class AuctionThumbnailCacheChangeListener implements MapChangeListener<Auction, Parent> {
    private final List<Node> flow;

    @Override
    public void onChanged(Change<? extends Auction, ? extends Parent> change) {
        if (change.wasAdded()) {
            val thumbnail = change.getValueAdded();
            if (!flow.contains(thumbnail)) flow.add(thumbnail);
        }
        if (change.wasRemoved()) {
            val thumbnail = change.getValueRemoved();
            flow.remove(thumbnail);
        }
    }
}
