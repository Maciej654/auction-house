package pl.poznan.put.model.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import javax.persistence.*;
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

    @Column(name = "RATING")
    private int rating;

    @Column(name = "text_desc")
    private String comment;


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
