package pl.poznan.put.model.auction;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.poznan.put.model.user.User;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "AUCTIONS")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "DISCRIMINATOR")
public abstract class Auction {
    public enum Status {
        PLANNED,
        CREATED,
        BIDDING,
        FINISHED,
        CANCELLED
    }

    @Id
    @Column(name = "AUCTION_ID")
    private long id;

    @Column(name = "AUCTION_NAME")
    private String auctionName;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "SELLER")
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
}
