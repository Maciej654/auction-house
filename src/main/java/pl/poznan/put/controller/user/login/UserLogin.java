package pl.poznan.put.controller.user.login;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.password.hasher.PasswordHasher;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Slf4j
public class UserLogin {
    @FXML
    private Label errorLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    private PasswordHasher hasher;

    private TypedQuery<User> loginQuery;

    @Getter
    private SimpleObjectProperty<User> userProperty;

    @FXML
    private void initialize() {
        log.info("initialize");
        hasher = PasswordHasher.defaultInstance();
        val em = EntityManagerProvider.getEntityManager();
        if (em != null) loginQuery = em.createNamedQuery(User.QUERY_CHECK_LOGIN, User.class);
        userProperty = new SimpleObjectProperty<>();
    }

    @FXML
    private void loginButtonClick() {
        log.info("login");

        if (hasher == null) {
            log.error("hasher is null");
            return;
        }

        if (loginQuery == null) {
            log.error("login query is null");
            return;
        }

        //TODO validate user email and password

        val email = emailTextField.getText();
        val hash  = hasher.apply(passwordField.getText());
        passwordField.clear();

        loginQuery.setParameter(User.PARAM_EMAIL, email);
        loginQuery.setParameter(User.PARAM_HASH, hash);

        try {
            val user = loginQuery.getSingleResult();
            userProperty.set(user);
        }
        catch (NoResultException e) {
            errorLabel.setText("Invalid email or password");
        }
    }

    @FXML
    private void registerButtonClick() {
        log.info("register");
        //TODO open `user-info` window with parameter `create`
    }
}
