package pl.poznan.put.model.shopping.cart.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.rating.Rating;
import pl.poznan.put.model.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "ITEMS_IN_SHOPPING_CARTS")
@IdClass(ShoppingCartItem.ShoppingCartId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem implements Serializable {
    @Id
    @JoinColumn(name = "AUCTION", referencedColumnName = "AUCTION_ID")
    @ManyToOne
    private Auction auction;

    @Id
    @JoinColumn(name = "BUYER", referencedColumnName = "EMAIL")
    @ManyToOne
    private User buyer;

    @OneToOne(fetch=FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumns({
            @JoinColumn(name="auction", referencedColumnName="auction"),
            @JoinColumn(name="buyer", referencedColumnName="reviewer")
    })
    private Rating rating;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShoppingCartId implements Serializable {
        private Auction auction;
        private User    buyer;
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
               "auction=" + auction.getId() +
               ", buyer=" + buyer.getEmail() +
               '}';
    }
}
