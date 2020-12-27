package pl.poznan.put.model.watch.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "WATCH_LISTS")
@IdClass(WatchList.WatchListId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchList {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WatchListId implements Serializable {
        private Auction auction;
        private User    follower;
        private String  name;
    }

    @Id
    @Column(name = "AUCTION")
    @ManyToOne
    private Auction auction;

    @Id
    @Column(name = "FOLLOWER")
    @ManyToOne
    private User follower;

    @Id
    @Column(name = "NAME")
    private String name;
}
