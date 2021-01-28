package pl.poznan.put.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import pl.poznan.put.model.ad.personal.PersonalAd;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.delivery.preference.DeliveryPreference;
import pl.poznan.put.model.follower.Follower;
import pl.poznan.put.model.rating.Rating;
import pl.poznan.put.model.shopping.cart.item.ShoppingCartItem;
import pl.poznan.put.model.watch.list.item.WatchListItem;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
@NamedQuery(
        name = User.QUERY_CHECK_LOGIN,
        query = "select user from User user where user.email = :" + User.PARAM_EMAIL + " and user.hash = :" + User.PARAM_HASH
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User implements Serializable {
    public static final String QUERY_CHECK_LOGIN = "User.QUERY_CHECK_LOGIN";
    public static final String PARAM_EMAIL       = "email";
    public static final String PARAM_HASH        = "hash";

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

    @OneToMany(mappedBy = "recipient")
    private Collection<PersonalAd> ads;

    public void refreshAds() {
        val em    = EntityManagerProvider.getEntityManager();
        val query = em.createNamedQuery(PersonalAd.QUERY_FIND_BY_RECIPIENT, PersonalAd.class);
        query.setParameter(PersonalAd.PARAM_RECIPIENT, this);
        ads = query.getResultList();
    }

    @OneToMany(mappedBy = "reviewer")
    private Collection<Rating> sentRatings;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Collection<Auction> auctions;

    public void refreshAuctions() {
        val em    = EntityManagerProvider.getEntityManager();
        val query = em.createNamedQuery(Auction.QUERY_FIND_ALL_BY_SELLER, Auction.class);
        query.setParameter(Auction.PARAM_SELLER, this);
        auctions = query.getResultList();
    }

    public enum FollowAction {
        FOLLOW,
        UNFOLLOW
    }

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

    @Override
    public String toString() {
        return "User{" +
               "email='" + email + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", hash='" + hash + '\'' +
               ", birthday=" + birthday +
               '}';
    }
}
