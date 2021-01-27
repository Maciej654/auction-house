package pl.poznan.put.controller.rating;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.rating.Rating;
import pl.poznan.put.model.shopping.cart.item.ShoppingCartItem;
import pl.poznan.put.model.user.User;
import pl.poznan.put.popup.PopUpWindow;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class RatingCreator {
    @FXML
    public               ChoiceBox<String> choiceBox;
    @FXML
    public               Button            confirmButton;
    @FXML
    public               TextField         ratingEntry;
    @FXML
    public               TextField         commentEntry;
    private static final EntityManager     entityManager = EntityManagerProvider.getEntityManager();
    private              User              user;

    @FXML
    private void initialize() {
        log.info("initialize");

        //todo maybe make presentation more readable
        user = Objects.requireNonNull(entityManager)
                      .find(User.class, "jacek.hercog@gmail.com");//toDO get the User from login view
        List<String> collect = shoppingCartItemStream()
                .filter(i -> i.getRating() == null) //for some reasons this can't be done in the query
                .map(ShoppingCartItem::getAuction)
                .map(this::auctionAttributes).collect(Collectors.toList());
        ObservableList<String> x = FXCollections.observableArrayList(collect);
        choiceBox.setItems(x);
    }

    @FXML
    public void buttonClicked() {
        if (!validate()) return;
        int    score = Integer.parseInt(ratingEntry.getText());
        String value = choiceBox.getValue();
        Auction auction = shoppingCartItemStream().map(ShoppingCartItem::getAuction)
                                                  .filter(a -> auctionAttributes(a).equals(value))
                                                  .findAny()
                                                  .orElse(null);
        if (auction == null) return;
        Rating            rating      = new Rating(auction, user, score, commentEntry.getText());
        EntityTransaction transaction = Objects.requireNonNull(entityManager).getTransaction();
        transaction.begin();
        entityManager.merge(rating); //persist z jakiegos powodu rzuca error
        transaction.commit();
        refresh();
    }

    private void refresh() {
        Objects.requireNonNull(entityManager).clear(); //whe can't use cached values
        List<String> collect = shoppingCartItemStream()
                .filter(i -> i.getRating() == null) //for some reasons this can't be done in the query
                .map(ShoppingCartItem::getAuction)
                .map(this::auctionAttributes).collect(Collectors.toList());
        ObservableList<String> x = FXCollections.observableArrayList(collect);
        choiceBox.setItems(x);
    }

    private boolean validate() {
        int score;
        try {
            score = Integer.parseInt(ratingEntry.getText());
        }
        catch (NumberFormatException e) {
            PopUpWindow.display("Incorrect format");
            return false;
        }
        if (score < 0 || score > 100) {
            PopUpWindow.display("Number must be in range of 0 - 100");
            return false;
        }
        if (choiceBox.getValue() == null) {
            PopUpWindow.display("You must choose an auction");
            return false;
        }
        return true;
    }

    private Stream<ShoppingCartItem> shoppingCartItemStream() {
        TypedQuery<ShoppingCartItem> query = Objects.requireNonNull(entityManager)
                                                    .createQuery("select item from ShoppingCartItem item where item" +
                                                                 ".buyer = :buyer and item.auction.status = :status",
                                                                 ShoppingCartItem.class);
        query.setParameter("buyer", user);
        query.setParameter("status", Auction.Status.FINISHED);
        return query.getResultStream();
    }

    private String auctionAttributes(Auction auction) {
        return "AUCTION NAME : " + auction.getAuctionName() +
               ", ITEM NAME :" + auction.getItemName() +
               ", SELLER : " + auction.getSeller().getEmail() +
               ", ENDDATE :" + auction.getEndDate();

    }
}


