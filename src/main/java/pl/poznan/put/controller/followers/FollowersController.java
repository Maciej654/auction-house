package pl.poznan.put.controller.followers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.followers.buttons.FollowButton;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FollowersController {
    @FXML
    public TableView<Data>           tableView;
    @FXML
    public TableColumn<Data, String> userColumn;
    @FXML
    public TableColumn<Data, Button> actionColumn;
    @FXML
    public TextField                 userEntry;
    @FXML
    public Button                    allUsersButton;

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    private User user;

    @AllArgsConstructor
    @lombok.Data
    public static class Data {
        String user;
        Button button;
    }

    @FXML
    private void initialize() {
        log.info("initialize");

        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    public void setUp() {
        if (em == null) { return; }
        user = em.find(User.class, "hercogmaciej@gmail.com"); //toDo integrate with other views
        showAllRows();
    }

    @FXML
    public void userEntered(ActionEvent actionEvent) {
        if (em == null) { return; }

        TypedQuery<User> query = em.createQuery("select user from User user where user <> :user", User.class);
        query.setParameter("user", user);
        List<Data> rows = query.getResultStream()
                               .filter(user -> user.getEmail()
                                                   .toUpperCase()
                                                   .contains(userEntry.getText().toUpperCase()))
                               .map(u -> new Data(u.getEmail(), new FollowButton(user, u)))
                               .collect(Collectors.toList());
        ObservableList<Data> obs = FXCollections.observableArrayList(rows);
        tableView.setItems(obs);
    }

    @FXML
    public void allUsersClicked(ActionEvent actionEvent) {
        showAllRows();
    }

    private void showAllRows() {
        if (em == null) { return; }
        TypedQuery<User> query = em.createQuery("select user from User user where user <> :user", User.class);
        query.setParameter("user", user);
        List<Data> rows = query.getResultStream()
                               .map(u -> new Data(u.getEmail(), new FollowButton(user, u)))
                               .collect(Collectors.toList());
        ObservableList<Data> obs = FXCollections.observableArrayList(rows);
        tableView.setItems(obs);
    }
}
