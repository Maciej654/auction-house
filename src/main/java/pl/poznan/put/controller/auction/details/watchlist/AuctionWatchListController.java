package pl.poznan.put.controller.auction.details.watchlist;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;
import pl.poznan.put.model.watch.list.item.WatchListItem;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class AuctionWatchListController {

    @FXML
    private Button addToExistingButton;

    @FXML
    private ChoiceBox<String> watchListChoiceBox;

    @FXML
    private TextField newWatchListEntry;

    @FXML
    private Button addToNewListButton;

    @FXML
    private Label errorLabel;

    @Getter
    private final ObjectProperty<Auction> auctionProperty = new SimpleObjectProperty<>();

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    @FXML
    private void initialize(){
        auctionProperty.addListener((observable, oldValue, newValue) -> setUp());
    }
    private void setUp(){
        if(em == null) { return; }
        Auction auction = auctionProperty.get();
        User user = CurrentUser.getLoggedInUser();

        watchListChoiceBox.setItems(FXCollections.observableArrayList(viableOptions()));
        watchListChoiceBox.getSelectionModel().selectFirst();

        addToExistingButton.setOnAction(a -> {
            if(watchListChoiceBox.getValue() == null){
                errorLabel.setText("No value specified");
                return;
            }
            String name = watchListChoiceBox.getValue();
            addDeliveryPreference(name);
            watchListChoiceBox.getItems().remove(name);
            errorLabel.setText(null);
        });

        addToNewListButton.setOnAction(a -> {
            if(newWatchListEntry.getText() == null || newWatchListEntry.getText().equals("")){
                errorLabel.setText("No value specified");
                return;
            }
            var watchListItem = em.find(WatchListItem.class, new WatchListItem.WatchListId(auction,user,newWatchListEntry.getText()));
            if(watchListItem != null){
                errorLabel.setText("auction already on this list");
                return;
            }
            if(watchListChoiceBox.getItems().contains(newWatchListEntry.getText())){
                errorLabel.setText("list already exists");
                return;
            }
            addDeliveryPreference(newWatchListEntry.getText());
            newWatchListEntry.setText(null);
            errorLabel.setText(null);
        });
    }
    private List<String> viableOptions(){
        if(em == null) {
            return new ArrayList<>();
        }
        Auction auction = auctionProperty.get();
        User user = CurrentUser.getLoggedInUser();

        var listsOfUserQuery = em.createQuery("select distinct list.name from WatchListItem list where list.follower = :user",String.class);
        listsOfUserQuery.setParameter("user", user);
        List<String> listsOfUser = listsOfUserQuery.getResultList();

        var listsOfAuctionQuery = em.createQuery("select distinct list.name from WatchListItem list where list.auction = :auction",String.class);
        listsOfAuctionQuery.setParameter("auction", auction);
        List<String> listsOfAuction = listsOfAuctionQuery.getResultList();

        listsOfUser.removeAll(listsOfAuction);
        return listsOfUser;

    }

    private void addDeliveryPreference(String name){
        if(em == null) { return; }
        Auction auction = auctionProperty.get();
        User user = CurrentUser.getLoggedInUser();

        try{
            WatchListItem watchListItem = new WatchListItem(auction, user, name);
            var transaction = em.getTransaction();
            transaction.begin();
            em.merge(watchListItem);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
