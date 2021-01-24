package pl.poznan.put.controller.delivery;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.poznan.put.logic.common.validation.empty.AbstractNotEmptyPropertyValidator;
import pl.poznan.put.model.delivery.preference.DeliveryPreference;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeliveryCreatorController {
    @FXML
    public Button okButton;

    @FXML
    public TextField deliveryAddressEntry;

    @FXML
    public TableView<Data> table;

    @FXML
    public TableColumn<Data, String> addressColumn;

    @FXML
    public TableColumn<Data, Button> buttonColumn;

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    public Label errorLabel;

    private User user;

    @lombok.Data
    @AllArgsConstructor
    public static class Data{
        private String address;
        private Button button;
    }

    private class DeleteButton extends Button {

        @Getter
        private final DeliveryPreference deliveryPreference;

        public DeleteButton(DeliveryPreference deliveryPreference) {
            super("DELETE");
            this.deliveryPreference = deliveryPreference;
            this.setOnAction(a -> deleteAuction());
        }

        private void deleteAuction(){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "are you sure you want to delete this", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.NO) { return; }
            if(em != null){
                var transaction = em.getTransaction();
                transaction.begin();
                em.remove(em.contains(deliveryPreference) ? deliveryPreference : em.merge(deliveryPreference));
                transaction.commit();
                refreshView();
            }
        }
    }

    @FXML
    private void initialize() {
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        buttonColumn.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    public void setup(){
        refreshView();
        if(em != null){
            user = em.find(User.class, "hercogmaciej@gmail.com"); //toDo connect with other views
        }

    }

    private void refreshView() {
        if(em != null){
            em.clear();
            var query = em.createQuery("select dp from DeliveryPreference dp where dp.user = user", DeliveryPreference.class);
            var addresses = query.getResultStream()
                    .map(dp -> new Data(dp.getAddress(), new DeleteButton(dp)))
                    .collect(Collectors.toList());
            var FXAddresses = FXCollections.observableArrayList(addresses);
            table.setItems(FXAddresses);
        }
        deliveryAddressEntry.setText("");
        errorLabel.setText("");
    }


    private boolean validate(String address){
        if(em == null){
            return false;
        }
        em.clear();
        var query = em.createQuery("select dp from DeliveryPreference dp where dp.user = :user and dp.address = :address",DeliveryPreference.class);
        query.setParameter("user", user);
        query.setParameter("address", address);
        int size = query.getResultList().size();
        if(size > 0){
            errorLabel.setText("This address already exists");
            return false;
        }
        if(address == null || address.length() == 0){
            errorLabel.setText("The field cannot be empty");
            return false;
        }
        return true;
    }
    @FXML
    public void okButtonPressed(ActionEvent actionEvent) {
        if(em == null){
            return;
        }
        String address = deliveryAddressEntry.getText();

        if(!validate(address)){
            return;
        }

        DeliveryPreference dp = new DeliveryPreference(user,address);
        var transaction = em.getTransaction();
        transaction.begin();
        em.merge(dp);
        transaction.commit();
        refreshView();
    }

}
