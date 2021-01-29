package pl.poznan.put.model.auction;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.val;
import pl.poznan.put.model.ad.Ad;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.picture.Picture;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "AUCTIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"AUCTION_NAME", "ITEM_NAME", "SELLER"}))
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "DISCRIMINATOR")
@NamedQuery(
        name = Auction.QUERY_FIND_BY_UNIQUE_KEY,
        query = "select count(auction) from Auction auction where auctionName = :" + Auction.PARAM_AUCTION_NAME + " " +
                "and " +
                "itemName = :" + Auction.PARAM_ITEM_NAME + " and seller = :" + Auction.PARAM_SELLER
)
@NamedQuery(
        name = Auction.QUERY_FIND_ALL_BY_SELLER,
        query = "select auction from Auction auction where seller = :" + Auction.PARAM_SELLER
)
public abstract class Auction implements Serializable {
    public static final String QUERY_FIND_BY_UNIQUE_KEY = "Auction.QUERY_FIND_BY_UNIQUE_KEY";
    public static final String QUERY_FIND_ALL_BY_SELLER = "Auction.QUERY_FIND_ALL_BY_SELLER";

    public static final String PARAM_AUCTION_NAME = "auctionName";
    public static final String PARAM_ITEM_NAME    = "itemName";
    public static final String PARAM_SELLER       = "seller";

    public static final Comparator<Auction> LATEST_FIRST = Comparator.comparing(Auction::getCreationDate).reversed();

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUCTIONS_SEQ")
    @SequenceGenerator(name = "AUCTIONS_SEQ", allocationSize = 1)
    @Column(name = "AUCTION_ID")
    private long id;

    @Column(name = "AUCTION_NAME")
    private String auctionName;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @JoinColumn(name = "SELLER", referencedColumnName = "EMAIL")
    @ManyToOne
    private User seller;

    @Column(name = "ITEM_DESCRIPTION")
    private String itemDescription;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    public void refreshEndDate() {
        endDate = EntityManagerProvider.getEntityManager()
                                       .createQuery("select auction.endDate from Auction auction where auction.id = :id", LocalDateTime.class)
                                       .setParameter("id", id)
                                       .getSingleResult();
    }

    @Column(name = "PRICE")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISCRIMINATOR", nullable = false, insertable = false, updatable = false)
    private Type type;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private Collection<AuctionLog> logs;

    public void addLog(AuctionLog log) {
        if (logs == null) logs = new ArrayList<>();
        logs.add(log);
    }

    @OneToOne(mappedBy = "auction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Ad ad;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<Picture> pictures;

    public enum Type {
        BOOK,
        CAR,
        DEFAULT,
        PHONE
    }

    public enum Status implements Serializable {
        CREATED,
        BIDDING,
        IN_SHOPPING_CART,
        FINISHED,
        CANCELLED
    }

    public boolean isActive() {
        return status == Status.CREATED || status == Status.BIDDING;
    }

    public static List<Auction> getAuctions() {
        val em = EntityManagerProvider.getEntityManager();
        if (em != null) return em.createQuery("SELECT auction from Auction auction", Auction.class).getResultList();
        else return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "Auction{" +
               "id=" + id +
               ", auctionName='" + auctionName + '\'' +
               ", itemName='" + itemName + '\'' +
               ", seller=" + seller.getEmail() +
               ", itemDescription='" + itemDescription + '\'' +
               ", creationDate=" + creationDate +
               ", endDate=" + endDate +
               ", price=" + price +
               ", status=" + status +
               ", ad=" + ad +
               ", pictures=" + pictures +
               '}';
    }

    public String getCategory() {
        return this.type.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return id == auction.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
