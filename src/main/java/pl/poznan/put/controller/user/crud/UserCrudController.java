package pl.poznan.put.controller.user.crud;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.controller.common.AbstractValidatedController;
import pl.poznan.put.logic.common.validation.PropertyValidator;
import pl.poznan.put.logic.common.validation.alpha.AlphaPropertyValidator;
import pl.poznan.put.logic.user.exception.EmailAlreadyInUseException;
import pl.poznan.put.logic.user.validation.UserBirthdayValidator;
import pl.poznan.put.logic.user.validation.UserConfirmPasswordValidator;
import pl.poznan.put.logic.user.validation.UserEmailValidator;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.password.hasher.PasswordHasher;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;
import pl.poznan.put.util.validation.Validation;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@Slf4j
public abstract class UserCrudController extends AbstractValidatedController {
    @FXML
    protected ImageView emailWarning;

    @FXML
    protected ImageView firstNameWarning;

    @FXML
    protected ImageView lastNameWarning;

    @FXML
    protected ImageView birthdayWarning;

    @FXML
    protected ImageView passwordWarning;

    @FXML
    protected ImageView confirmPasswordWarning;

    @FXML
    protected Pane spacePane;

    @FXML
    protected TextField emailTextField;

    @FXML
    protected TextField firstNameTextField;

    @FXML
    protected TextField lastNameTextField;

    @FXML
    protected DatePicker birthdayDatePicker;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected PasswordField confirmPasswordField;

    @FXML
    protected Button actionButton;

    @FXML
    protected Label errorLabel;

    @Setter
    protected Runnable backCallback;

    @Setter
    protected Consumer<User> operationCallback = user -> {};

    protected final EntityManager em = EntityManagerProvider.getEntityManager();

    protected final PasswordHasher hasher = PasswordHasher.defaultInstance();

    private final BooleanProperty emailValid           = new SimpleBooleanProperty(true);
    private final BooleanProperty firstNameValid       = new SimpleBooleanProperty(true);
    private final BooleanProperty lastNameValid        = new SimpleBooleanProperty(true);
    private final BooleanProperty birthdayValid        = new SimpleBooleanProperty(true);
    private final BooleanProperty passwordValid        = new SimpleBooleanProperty(true);
    private final BooleanProperty confirmPasswordValid = new SimpleBooleanProperty(true);

    protected abstract PropertyValidator<String> getPasswordValidator();

    private final BooleanBinding userInfoValid = emailValid.and(firstNameValid)
                                                           .and(lastNameValid)
                                                           .and(birthdayValid)
                                                           .and(passwordValid)
                                                           .and(confirmPasswordValid);

    @Override
    protected void installValidation() {
        // email
        Validation.install(
                emailTextField.textProperty(),
                new UserEmailValidator(),
                emailValid,
                emailWarning
        );

        // first name
        Validation.install(
                firstNameTextField.textProperty(),
                new AlphaPropertyValidator("First name"),
                firstNameValid,
                firstNameWarning
        );

        // last name
        Validation.install(
                lastNameTextField.textProperty(),
                new AlphaPropertyValidator("Last name"),
                lastNameValid,
                lastNameWarning
        );

        // birthday
        Validation.install(
                birthdayDatePicker.valueProperty(),
                new UserBirthdayValidator(),
                birthdayValid,
                birthdayWarning
        );

        // password
        Validation.install(
                passwordField.textProperty(),
                this.getPasswordValidator(),
                passwordValid,
                passwordWarning
        );

        // confirm password (1)
        Validation.install(
                confirmPasswordField.textProperty(),
                new UserConfirmPasswordValidator(passwordField.textProperty()),
                confirmPasswordValid,
                confirmPasswordWarning
        );

        // confirm password (2)
        Validation.install(
                passwordField.textProperty(),
                new UserConfirmPasswordValidator(confirmPasswordField.textProperty()),
                confirmPasswordValid,
                confirmPasswordWarning
        );
    }

    @Override
    protected void setupInitialValues() {
        emailTextField.setText(null);
        firstNameTextField.setText(null);
        lastNameTextField.setText(null);
        birthdayDatePicker.setValue(LocalDate.now());
        passwordField.setText(null);
        confirmPasswordField.setText(null);
    }

    protected void unbindActionButton() {
        actionButton.disableProperty().unbind();
    }

    protected void bindActionButton() {
        actionButton.disableProperty().bind(userInfoValid.not());
    }

    @Override
    @FXML
    protected void initialize() {
        super.initialize();

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthdayDatePicker.setConverter(new UserCrudController.SimpleDateConverter(formatter));

        HBox.setHgrow(spacePane, Priority.ALWAYS);

        bindActionButton();
    }

    public void setEmail(String email) {
        emailTextField.setText(email);
    }

    public void setPassword(String password) {
        passwordField.setText(password);
        confirmPasswordField.setText(StringUtils.EMPTY);
    }

    public void setUser(User user) {
        setEmail(user.getEmail());
        firstNameTextField.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        birthdayDatePicker.setValue(user.getBirthday());
        setPassword(StringUtils.EMPTY);
    }

    public User getUser() {
        return User.builder()
                   .email(emailTextField.getText())
                   .firstName(StringUtils.capitalize(firstNameTextField.getText()))
                   .lastName(StringUtils.capitalize(lastNameTextField.getText()))
                   .birthday(birthdayDatePicker.getValue())
                   .hash(hasher.apply(passwordField.getText()))
                   .build();
    }

    abstract protected void crudOperation(User user) throws Exception;

    @FXML
    protected void actionButtonClick() {
        log.info("action");

        if (hasher == null) {
            log.error("hasher is null");
            return;
        }

        if (em == null) {
            log.error("entity manager is null");
            return;
        }

        new Thread(() -> {
            val transaction = em.getTransaction();
            transaction.begin();
            try {
                Platform.runLater(() -> {
                    unbindActionButton();
                    actionButton.setDisable(true);
                    actionButton.setCursor(Cursor.WAIT);
                });
                val user = getUser();
                crudOperation(user);
                transaction.commit();
                log.info("commit success");
                Platform.runLater(() -> operationCallback.accept(user));
            }
            catch (EmailAlreadyInUseException e) {
                Platform.runLater(() -> errorLabel.setText(e.getMessage()));
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            finally {
                Platform.runLater(() -> {
                    bindActionButton();
                    actionButton.setCursor(Cursor.HAND);
                });
                if (transaction.isActive()) transaction.rollback();
            }
        }).start();
    }

    @FXML
    protected void backButtonClick() {
        backCallback.run();
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
