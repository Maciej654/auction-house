package pl.poznan.put.model.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.shopping.cart.ShoppingCart;
import pl.poznan.put.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RATINGS")
@IdClass(ShoppingCart.ShoppingCartId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingId {
        private Auction auction;
        private User    reviewer;
    }

    @Id
    @Column(name = "AUCTION")
    @ManyToOne
    private Auction auction;

    @Id
    @Column(name = "REVIEWER")
    @ManyToOne
    private User reviewer;
}
