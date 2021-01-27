package pl.poznan.put.controller.auction.shoppingHistory;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.shopping.cart.item.ShoppingCartItem;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ShoppingHistoryController {
    @FXML
    private TableView<Data> table;

    @FXML
    private TableColumn<Data, String> auctionColumn;

    @FXML
    private TableColumn<Data, String> itemColumn;

    @FXML
    private TableColumn<Data, String> destinationColumn;

    @FXML
    private TableColumn<Data, String> priceColumn;

    @FXML
    private TableColumn<Data, String> sellerColumn;

    private User user;

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    @lombok.Data
    @SuperBuilder
    public static class Data {
        private String auction;
        private String item;
        private String destination;
        private String price;
        private String seller;
    }

    @FXML
    private void initialize() {
        log.info("initialize");

        auctionColumn.setCellValueFactory(new PropertyValueFactory<>("auction"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("seller"));
    }

    public void setup() {
        if (em == null) { return; }
        user = em.find(User.class, "hercogmaciej@gmail.com"); //toDo connect with other views
        var query = em.createQuery("select item from ShoppingCartItem item where item.buyer = :buyer and item.auction" +
                                   ".status = :status", ShoppingCartItem.class);
        query.setParameter("buyer", user);
        query.setParameter("status", Auction.Status.FINISHED);

        List<Data> rows = query.getResultStream().map(ShoppingCartItem::getAuction)
                               .map(a -> Data.builder()
                                             .seller(a.getSeller().getEmail())
                                             .item(a.getItemName())
                                             .destination(extractDestination(a))
                                             .price(String.valueOf(a.getPrice()))
                                             .auction(a.getAuctionName()).build())
                               .collect(Collectors.toList());
        table.setItems(FXCollections.observableArrayList(rows));

    }

    private String extractDestination(Auction auction) {
        if (em == null) return StringUtils.EMPTY;
        val query = em.createQuery("select log from AuctionLog  log where log.auction = :auction", AuctionLog.class);
        query.setParameter("auction", auction);
        return query.getResultStream()
                    .map(AuctionLog::getDescription)
                    .filter(d -> d.startsWith("shipping destination requested: "))
                    .map(d -> d.substring(d.indexOf(":") + 2))
                    .findFirst()
                    .orElse(StringUtils.EMPTY);

    }
}
