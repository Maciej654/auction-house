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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "AUCTIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"AUCTION_NAME", "ITEM_NAME", "SELLER"}))
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "DISCRIMINATOR")
public abstract class Auction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "PRICE")
    private double price;

    @Column(name = "STATUS")
    private Status status;

    @Column(name = "DISCRIMINATOR")
    private Type type;

    @OneToMany(mappedBy = "auction")
    private Collection<AuctionLog> logs;

    @OneToOne(mappedBy = "auction", fetch = FetchType.LAZY)
    private Ad ad;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private Collection<Picture> pictures;

//    @OneToOne(mappedBy = "auction", fetch = FetchType.LAZY)
//    private Rating rating;

    public enum Type {
        BOOK,
        CAR,
        DEFAULT,
        PHONE
    }

    public enum Status implements Serializable {
        PLANNED,
        CREATED,
        BIDDING,
        FINISHED,
        CANCELLED
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
}
