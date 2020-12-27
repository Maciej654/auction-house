package pl.poznan.put.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.ad.personal.PersonalAd;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.delivery.preference.DeliveryPreference;
import pl.poznan.put.model.follower.Follower;
import pl.poznan.put.model.rating.Rating;
import pl.poznan.put.model.shopping.cart.item.ShoppingCartItem;
import pl.poznan.put.model.watch.list.item.WatchListItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "HASH")
    private String hash;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthday;

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
    private Collection<Follower> followersRaw;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private Collection<Follower> followeesRaw;

    @OneToMany(mappedBy = "user")
    private Collection<DeliveryPreference> deliveryPreferences;

    @OneToMany(mappedBy = "buyer")
    private Collection<ShoppingCartItem> shoppingCartItems;

    @OneToMany(mappedBy = "follower")
    private Collection<WatchListItem> watchListItems;

    @OneToMany(mappedBy = "actor")
    private Collection<AuctionLog> logs;

//    @OneToOne(mappedBy = "recipient")
//    private PersonalAd ad;

    @OneToMany(mappedBy = "reviewer")
    private Collection<Rating> sentRatings;

    @OneToMany(mappedBy = "seller")
    private Collection<Auction> auctions;

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public Collection<User> getFollowers() {
        return followersRaw.stream()
                           .map(Follower::getFollower)
                           .collect(Collectors.toList());
    }

    public Collection<User> getFollowees() {
        return followeesRaw.stream()
                           .map(Follower::getFollowee)
                           .collect(Collectors.toList());
    }

    public Map<String, List<Auction>> getWatchLists() {
        Collector<Auction, ?, List<Auction>> auctionList = Collectors.toList();

        Collector<WatchListItem, ?, List<Auction>> watchList =
                Collectors.mapping(WatchListItem::getAuction, auctionList);

        Collector<WatchListItem, ?, Map<String, List<Auction>>> watchLists =
                Collectors.groupingBy(WatchListItem::getName, watchList);

        return watchListItems.stream()
                             .collect(watchLists);
    }

//    public Collection<Rating> getRatings() {
//        return auctions.stream()
//                       .map(Auction::getRating)
//                       .filter(Objects::nonNull)
//                       .collect(Collectors.toList());
//    }
}
