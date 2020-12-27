package pl.poznan.put.model.shopping.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ITEMS_IN_SHOPPING_CARTS")
@IdClass(ShoppingCart.ShoppingCartId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShoppingCartId implements Serializable {
        private Auction auction;
        private User    buyer;
    }

    @Id
    @Column(name = "AUCTION")
    @ManyToOne
    private Auction auction;

    @Id
    @Column(name = "BUYER")
    @ManyToOne
    private User buyer;
}
