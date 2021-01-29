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
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import java.util.ArrayList;
import java.util.List;

public class AuctionWatchListController {
    @FXML
    public Button removeFromListButton;

    @FXML
    public Button removeTheListButton;

    @FXML
    public ChoiceBox<String> removeFromListChoiceBox;

    @FXML
    public ChoiceBox<String> removeTheListChoiceBox;

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

        setAllChoiceBoxes();
        addToExistingButton.setOnAction(a -> {
            if(watchListChoiceBox.getValue() == null){
                errorLabel.setText("No value specified");
                return;
            }
            String name = watchListChoiceBox.getValue();
            addToWatchList(name);
            errorLabel.setText(null);
            setAllChoiceBoxes();
        });

        addToNewListButton.setOnAction(a -> {
            if(newWatchListEntry.getText() == null || newWatchListEntry.getText().equals("")){
                errorLabel.setText("No value specified");
                return;
            }
            if(allListsForAuction().contains(newWatchListEntry.getText())){
                System.out.println("dupa1");
                errorLabel.setText("auction already on this list");
                return;
            }
            if(watchListChoiceBox.getItems().contains(newWatchListEntry.getText())){
                System.out.println("dupa2");
                errorLabel.setText("list already exists");
                return;
            }
            System.out.println("dupa3");
            addToWatchList(newWatchListEntry.getText());
            newWatchListEntry.setText(null);
            errorLabel.setText(null);
            setAllChoiceBoxes();
        });

        removeFromListButton.setOnAction(a -> {
            if(removeFromListChoiceBox.getValue() == null){
                errorLabel.setText("No value specified");
                return;
            }
            removeFromList();
            errorLabel.setText(null);
            setAllChoiceBoxes();
        });

        removeTheListButton.setOnAction(a -> {
            if(removeTheListChoiceBox.getValue() == null){
                errorLabel.setText("No value specified");
                return;
            }
            removeAllItemsFromList();
            errorLabel.setText(null);
            setAllChoiceBoxes();
        });
    }

    private void setAllChoiceBoxes() {
        watchListChoiceBox.setItems(FXCollections.observableArrayList(viableOptions()));
        removeFromListChoiceBox.setItems(FXCollections.observableArrayList(allListsForAuction()));
        removeTheListChoiceBox.setItems(FXCollections.observableArrayList(allListsOfUser()));
    }

    private void removeAllItemsFromList() {
        if(em == null){ return; }
        var query = em.createQuery("delete from WatchListItem item where item.follower = :user and item.name = :name");
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            query.setParameter("user", CurrentUser.getLoggedInUser());
            query.setParameter("name", removeTheListChoiceBox.getValue());
            query.executeUpdate();
            em.flush();
            em.clear();
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

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

    private List<String> allListsOfUser() {
        if (em == null) {
            return new ArrayList<>();
        }
        User user = CurrentUser.getLoggedInUser();
        var listsOfUserQuery = em.createQuery("select distinct list.name from WatchListItem list where list.follower = :user", String.class);
        listsOfUserQuery.setParameter("user", user);
        try {
            return listsOfUserQuery.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<String> allListsForAuction(){
        if(em == null) {
            return new ArrayList<>();
        }
        Auction auction = auctionProperty.get();
        var listsOfUserQuery = em.createQuery("select distinct list.name from WatchListItem list where list.auction = :auction",String.class);
        listsOfUserQuery.setParameter("auction", auction);
        try{
            return listsOfUserQuery.getResultList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
    private void addToWatchList(String name){
        if(em == null) { return; }
        Auction auction = auctionProperty.get();
        User user = CurrentUser.getLoggedInUser();

        try{
            WatchListItem watchListItem = new WatchListItem(auction, user, name);
            var transaction = em.getTransaction();
            transaction.begin();
            em.merge(watchListItem);
            em.flush();
            em.clear();
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void removeFromList() {
        if(em == null){ return; }
        var query = em.createQuery("delete from WatchListItem item where item.follower = :user and item.name = :name and item.auction = :auction");
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            query.setParameter("user", CurrentUser.getLoggedInUser());
            query.setParameter("name", removeFromListChoiceBox.getValue());
            query.setParameter("auction", auctionProperty.get());
            query.executeUpdate();
            em.flush();
            em.clear();
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
