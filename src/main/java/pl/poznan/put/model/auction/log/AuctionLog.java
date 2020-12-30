package pl.poznan.put.model.auction.log;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "AUCTIONS_LOGS")
@IdClass(AuctionLog.AuctionLogId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionLog implements Serializable {
    @Id
    @JoinColumn(name = "AUCTION", referencedColumnName = "AUCTION_ID")
    @ManyToOne
    private Auction auction;

    @Id
    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Column(name = "ACTION_DESCRIPTION")
    private String description;

    @JoinColumn(name = "ACTOR", referencedColumnName = "EMAIL")
    @ManyToOne
    private User actor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuctionLogId implements Serializable {
        private Auction       auction;
        private LocalDateTime timestamp;
    }

    @Override
    public String toString() {
        return "AuctionLog{" +
                "auction=" + auction.getId() +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                ", actor=" + actor.getEmail() +
                '}';
    }
}
