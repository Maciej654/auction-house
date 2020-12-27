package pl.poznan.put.model.auction.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "AUCTIONS_LOGS")
@IdClass(AuctionLog.AuctionLogId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionLog {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuctionLogId implements Serializable {
        private Auction auction;
        private long    timestamp;
    }

    @Id
    @Column(name = "AUCTION")
    @ManyToOne
    private Auction auction;

    @Id
    @Column(name = "TIMESTAMP")
    private long timestamp;

    @Column(name = "ACTION_DESCRIPTION")
    private String description;

    @Column(name = "ACTOR")
    @ManyToOne
    private User actor;
}
