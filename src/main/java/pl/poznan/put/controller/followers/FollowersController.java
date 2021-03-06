package pl.poznan.put.controller.followers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.followers.buttons.FollowButton;
import pl.poznan.put.logic.user.current.CurrentUser;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.callback.Callbacks;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FollowersController {
    @FXML
    public TableView<Data> tableView;

    @FXML
    public TableColumn<Data, String> userColumn;

    @FXML
    public TableColumn<Data, Button> actionColumn;

    @FXML
    public TextField userEntry;

    @FXML
    public Button allUsersButton;

    @FXML
    public Button userPageButton;

    @Setter
    private Runnable userPageCallback = Callbacks::noop;

    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    @AllArgsConstructor
    @lombok.Data
    public static class Data {
        String user;
        Button button;
    }

    @FXML
    private void initialize() {
        log.info("initialize");
        userPageButton.setOnAction(a -> userPageCallback.run());
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    public void setUp() {
        if (em == null) return;
        showAllRows();
    }

    @FXML
    public void userEntered() {
        if (em == null) return;
        User user = CurrentUser.getLoggedInUser();
        TypedQuery<User> query = em.createQuery("select user from User user where user <> :user", User.class);
        query.setParameter("user", user);
        List<Data> rows = query.getResultStream()
                               .filter(u -> u.getEmail()
                                                   .toUpperCase()
                                                   .contains(userEntry.getText().toUpperCase()))
                               .map(u -> new Data(u.getEmail(), new FollowButton(u, u)))
                               .collect(Collectors.toList());
        ObservableList<Data> obs = FXCollections.observableArrayList(rows);
        tableView.setItems(obs);
    }

    @FXML
    public void allUsersClicked() {
        showAllRows();
    }

    private void showAllRows() {
        if (em == null) return;
        User user = CurrentUser.getLoggedInUser();
        TypedQuery<User> query = em.createQuery("select user from User user where user <> :user", User.class);
        query.setParameter("user", user);
        List<Data> rows = query.getResultStream()
                               .map(u -> new Data(u.getFullName(), new FollowButton(user, u)))
                               .collect(Collectors.toList());
        ObservableList<Data> obs = FXCollections.observableArrayList(rows);
        tableView.setItems(obs);
    }
}
