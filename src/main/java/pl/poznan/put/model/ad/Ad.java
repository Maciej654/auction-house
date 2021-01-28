package pl.poznan.put.model.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ADS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(
        name = Ad.QUERY_FIND_ALL,
        query = "select ad from Ad ad"
)
public class Ad implements Serializable {
    public static final String QUERY_FIND_ALL = "Ad.QUERY_FIND_ALL";

    @Id
    @Column(name = "AUCTION")
    private long id;

    @JoinColumn(name = "AUCTION", referencedColumnName = "AUCTION_ID")
    @OneToOne
    private Auction auction;

    @Column(name = "SLOGAN")
    private String slogan;

    @Override
    public String toString() {
        return "Ad{" +
               "auction=" + auction.getId() +
               ", slogan='" + slogan + '\'' +
               '}';
    }
}
