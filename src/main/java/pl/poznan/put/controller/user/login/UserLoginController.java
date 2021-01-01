package pl.poznan.put.controller.user.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.password.hasher.PasswordHasher;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public class UserLoginController {
    @FXML
    private Label errorLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    private final PasswordHasher hasher = PasswordHasher.defaultInstance();

    private TypedQuery<User> loginQuery;

    @Setter
    private Consumer<User> loginCallback;

    @Setter
    private BiConsumer<String, String> registerCallback;

    @FXML
    private void initialize() {
        log.info("initialize");
        val em = EntityManagerProvider.getEntityManager();
        if (em != null) loginQuery = em.createNamedQuery(User.QUERY_CHECK_LOGIN, User.class);
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
            errorLabel.setText(StringUtils.EMPTY);
            val user = loginQuery.getSingleResult();
            loginCallback.accept(user);
        }
        catch (NoResultException e) {
            errorLabel.setText("Invalid email or password");
        }
    }

    @FXML
    private void registerButtonClick() {
        log.info("register");

        val email    = emailTextField.getText();
        val password = passwordField.getText();
        registerCallback.accept(email, password);
    }
}
