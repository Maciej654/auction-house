package pl.poznan.put.controller.shoppingCart;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.auction.log.AuctionLog;
import pl.poznan.put.model.delivery.preference.DeliveryPreference;
import pl.poznan.put.model.shopping.cart.item.ShoppingCartItem;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.callback.Callbacks;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ShoppingCartController {
    @FXML
    public TableView<Data> table;

    @FXML
    public TableColumn<Data, String> auctionNameColumn;

    @FXML
    public TableColumn<Data, String> itemNameColumn;

    @FXML
    public TableColumn<Data, String> sellerColumn;

    @FXML
    public TableColumn<Data, ChoiceBox<String>> addressColumn;

    @FXML
    public TableColumn<Data, Button> acceptColumn;

    @FXML
    public Label errorLabel;

    @FXML
    public Button userPageButton;

    private User user;

    @Setter
    Runnable userPageCallback = Callbacks::noop;

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    @lombok.Data
    public class Data {
        private String            auctionName;
        private String            itemName;
        private String            seller;
        private ChoiceBox<String> addresses;
        private Button            button;

        public Data(ShoppingCartItem shoppingCartItem) {
            auctionName = shoppingCartItem.getAuction().getAuctionName();
            itemName = shoppingCartItem.getAuction().getItemName();
            seller = shoppingCartItem.getAuction().getSeller().getEmail();
            addresses = new ChoiceBox<>();
            List<String> strings = shoppingCartItem.getBuyer()
                                                   .getDeliveryPreferences()
                                                   .stream()
                                                   .map(DeliveryPreference::getAddress)
                                                   .collect(Collectors.toList());
            addresses.setItems(FXCollections.observableArrayList(strings));
            button = new SelectButton(shoppingCartItem, addresses);
        }
    }

    private class SelectButton extends Button {
        private final ShoppingCartItem  shoppingCartItem;
        private final ChoiceBox<String> addresses;
        private final Alert             alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to " +
                                                                                        "pick this address?",
                                                          ButtonType.YES, ButtonType.NO);

        public SelectButton(ShoppingCartItem shoppingCartItem, ChoiceBox<String> addresses) {
            super("Select");
            this.shoppingCartItem = shoppingCartItem;
            this.addresses = addresses;
            this.setOnAction(a -> this.action());
        }

        private void action() {
            if (em == null) { return; }
            if (addresses.getValue() == null) {
                errorLabel.setText("you must choose destination address");
                return;
            }
            alert.showAndWait();
            if (alert.getResult() != ButtonType.YES) { return; }

            var auction = shoppingCartItem.getAuction();
            AuctionLog auctionLog = new AuctionLog(auction, LocalDateTime.now(),
                                                   "shipping destination requested: " + addresses
                                                           .getValue(), user);

            auction.setStatus(Auction.Status.FINISHED);
            var transaction = em.getTransaction();
            transaction.begin();
            em.merge(auction);
            em.merge(auctionLog);
            transaction.commit();
            refresh();
        }
    }

    @FXML
    private void initialize() {
        log.info("initialize");
        userPageButton.setOnAction(a -> userPageCallback.run());
        auctionNameColumn.setCellValueFactory(new PropertyValueFactory<>("auctionName"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("seller"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("addresses"));
        acceptColumn.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    public void setup(User user) {
        this.user = user;
        if (em == null) {
            return;
        }
        refresh();
    }

    private void refresh() {
        if (em == null) { return; }
        em.clear();
        var query = em.createQuery("select item from ShoppingCartItem item " +
                                   "where item.buyer = :user and item.auction.status = :status",
                                   ShoppingCartItem.class);
        query.setParameter("user", user);
        query.setParameter("status", Auction.Status.IN_SHOPPING_CART);

        List<Data> list = query.getResultStream().map(Data::new).collect(Collectors.toList());
        table.setItems(FXCollections.observableArrayList(list));
        errorLabel.setText(null);
    }
}
