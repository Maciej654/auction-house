package pl.poznan.put.model.watch.list.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "WATCH_LISTS")
@IdClass(WatchListItem.WatchListId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchListItem implements Serializable {
    @Id
    @JoinColumn(name = "AUCTION", referencedColumnName = "AUCTION_ID")
    @ManyToOne
    private Auction auction;

    @Id
    @JoinColumn(name = "FOLLOWER", referencedColumnName = "EMAIL")
    @ManyToOne
    private User follower;

    @Id
    @Column(name = "NAME")
    private String name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WatchListId implements Serializable {
        private Auction auction;
        private User    follower;
        private String  name;
    }

    @Override
    public String toString() {
        return "WatchListItem{" +
               "auction=" + auction.getId() +
               ", follower=" + follower.getEmail() +
               ", name='" + name + '\'' +
               '}';
    }
}
