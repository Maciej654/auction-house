package pl.poznan.put.model.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "RATINGS")
@IdClass(Rating.RatingId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating implements Serializable {
    @Id
    @JoinColumn(name = "AUCTION", referencedColumnName = "AUCTION_ID")
    @OneToOne
    private Auction auction;

    @Id
    @JoinColumn(name = "REVIEWER", referencedColumnName = "EMAIL")
    @ManyToOne
    private User reviewer;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingId implements Serializable {
        private Auction auction;
        private User    reviewer;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "auction=" + auction.getId() +
                ", reviewer=" + reviewer.getEmail() +
                '}';
    }
}
