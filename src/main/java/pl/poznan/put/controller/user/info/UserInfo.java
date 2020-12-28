package pl.poznan.put.controller.user.info;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.password.hasher.PasswordHasher;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@Slf4j
public class UserInfo {
    @FXML
    private Pane spacePane;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button saveButton;

    private Action action = Action.CREATE;

    public void setAction(Action action) {
        this.action = action;
        switch (action) {
            case CREATE:
                emailTextField.setEditable(true);
                saveButton.setText("Register");
                break;
            case UPDATE:
                emailTextField.setEditable(false);
                saveButton.setText("Save");
                break;
        }
    }

    @Setter
    private Runnable backCallback;

    @Setter
    private Consumer<User> saveCallback;

    private final EntityManager em = EntityManagerProvider.getEntityManager();

    private final PasswordHasher hasher = PasswordHasher.defaultInstance();

    @FXML
    private void initialize() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthdayDatePicker.setConverter(new SimpleDateConverter(formatter));
        HBox.setHgrow(spacePane, Priority.ALWAYS);
    }

    public void setEmail(String email) {
        emailTextField.setText(email);
    }

    public void setPassword(String password) {
        passwordField.setText(password);
    }

    public void setUser(User user) {
        setEmail(user.getEmail());
        firstNameTextField.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        birthdayDatePicker.setValue(user.getBirthday());
        setPassword(StringUtils.EMPTY);
    }

    @FXML
    private void saveButtonClick() {
        log.info("save");

        if (hasher == null) {
            log.error("hasher is null");
            return;
        }

        if (em == null) {
            log.error("entity manager is null");
            return;
        }

        //TODO validate user info

        val hash = hasher.apply(passwordField.getText());
        val user = User.builder()
                       .email(emailTextField.getText())
                       .firstName(firstNameTextField.getText())
                       .lastName(lastNameTextField.getText())
                       .birthday(birthdayDatePicker.getValue())
                       .hash(hash)
                       .build();

        val transaction = em.getTransaction();
        transaction.begin();
        try {
            switch (action) {
                case CREATE:
                    em.persist(user);
                    log.info("User '{}' created", user.getEmail());
                    //TODO register callback
                    break;
                case UPDATE:
                    em.merge(user);
                    log.info("User '{}' updated", user.getEmail());
                    break;
            }
            transaction.commit();
            saveCallback.accept(user);
        }
        catch (Throwable t) {
            log.error("An error occurred", t);
            transaction.rollback();
        }
    }

    public void backButtonClick() {
        backCallback.run();
    }

    public enum Action {
        CREATE,
        UPDATE
    }

    @RequiredArgsConstructor
    private static class SimpleDateConverter extends StringConverter<LocalDate> {
        private final DateTimeFormatter formatter;

        @Override
        public String toString(LocalDate object) {
            return object == null ? StringUtils.EMPTY : formatter.format(object);
        }

        @Override
        public LocalDate fromString(String string) {
            return StringUtils.isBlank(string) ? null : LocalDate.parse(string, formatter);
        }
    }
}
