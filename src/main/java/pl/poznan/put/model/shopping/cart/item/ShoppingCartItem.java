package pl.poznan.put.model.shopping.cart.item;

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
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ITEMS_IN_SHOPPING_CARTS")
@IdClass(ShoppingCartItem.ShoppingCartId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem implements Serializable {
    @Id
    @JoinColumn(name = "AUCTION")
    @ManyToOne
    private Auction auction;
    @Id
    @JoinColumn(name = "BUYER")
    @ManyToOne
    private User    buyer;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShoppingCartId implements Serializable {
        private Auction auction;
        private User    buyer;
    }
}
