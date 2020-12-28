package pl.poznan.put.controller.user.info;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

@Slf4j
public class UserInfo {
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

    @Setter
    private EntityManager em;

    @Setter
    private PasswordHasher hasher = PasswordHasher.of("MD5");

    public void setAction(Action action) {
        this.action = action;
        switch (action) {
            case CREATE:
                emailTextField.setEditable(true);
                saveButton.setText("REGISTER");
                break;
            case UPDATE:
                emailTextField.setEditable(false);
                saveButton.setText("UPDATE");
                break;
        }
    }

    @FXML
    private void initialize() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthdayDatePicker.setConverter(new SimpleDateConverter(formatter));
        em = EntityManagerProvider.getEntityManager();
    }

    public void setUser(User user) {
        emailTextField.setText(user.getEmail());
        firstNameTextField.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        birthdayDatePicker.setValue(user.getBirthday());
        passwordField.clear();
    }

    @FXML
    private void saveButtonClick() {
        if (hasher != null) {
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
                        //TODO create user
                        log.info("User '{}' created", user.getEmail());
                        break;
                    case UPDATE:
                        em.merge(user);
                        //TODO update user
                        log.info("User '{}' updated", user.getEmail());
                        break;
                }
                transaction.commit();
            }
            catch (Throwable t) {
                log.error("Unexpected error occurred", t);
                transaction.rollback();
            }
        }
        else log.error("Hasher is null");
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
