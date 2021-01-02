package pl.poznan.put.model.auction;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.poznan.put.model.ad.Ad;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.picture.Picture;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "AUCTIONS")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "DISCRIMINATOR")
public abstract class Auction implements Serializable {
    @Id
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
    private String discriminator;

    @OneToMany(mappedBy = "auction")
    private Collection<AuctionLog> logs;

    @OneToOne(mappedBy = "auction", fetch = FetchType.LAZY)
    private Ad ad;

    @OneToMany(mappedBy = "auction")
    private Collection<Picture> pictures;

//    @OneToOne(mappedBy = "auction", fetch = FetchType.LAZY)
//    private Rating rating;

    public enum Status implements Serializable {
        PLANNED,
        CREATED,
        BIDDING,
        FINISHED,
        CANCELLED
    }
    public static List<Auction> getAuctions() {
        EntityManager em = EntityManagerProvider.getEntityManager();
        TypedQuery<Auction> query = null;
        if (em != null) {
            query = em.createQuery("SELECT auction from Auction auction", Auction.class);
        }
        return  query.getResultList();
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

    public String getCategory(){
        return this.discriminator;
    }
}
