package pl.poznan.put.model.auction;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.poznan.put.model.ad.Ad;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.picture.Picture;
import pl.poznan.put.model.rating.Rating;
import pl.poznan.put.model.user.User;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

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

    @JoinColumn(name = "SELLER")
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

    @OneToMany(mappedBy = "auction")
    private Collection<AuctionLog> logs;

    @OneToOne(mappedBy = "auction")
    private Ad ad;

    @OneToMany(mappedBy = "auction")
    private Collection<Picture> pictures;

//    @OneToOne(mappedBy = "auction")
//    private Rating rating;

    public enum Status implements Serializable {
        PLANNED,
        CREATED,
        BIDDING,
        FINISHED,
        CANCELLED
    }
}
