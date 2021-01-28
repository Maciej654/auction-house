package pl.poznan.put.controller.user.crud.update;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.controller.user.crud.UserCrudController;
import pl.poznan.put.logic.common.validation.PropertyValidator;
import pl.poznan.put.logic.user.validation.UserUpdatePasswordValidator;
import pl.poznan.put.model.user.User;

import java.util.Objects;

@Slf4j
public class UserUpdateController extends UserCrudController {
    private User user;

    @Override
    protected PropertyValidator<String> getPasswordValidator() {
        return new UserUpdatePasswordValidator();
    }

    @Override
    public void setUser(User user) {
        super.setUser(user);
        this.user = user;
    }

    @Override
    public User getUser() {
        var user = super.getUser();
        if (Objects.nonNull(this.user)) {
            user = this.user.toBuilder()
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .birthday(user.getBirthday())
                            .hash(user.getHash())
                            .build();
            if (StringUtils.isBlank(passwordField.getText())) user.setHash(this.user.getHash());
        }
        return user;
    }

    @Override
    protected void crudOperation(User user) {
        log.info("update");
        Objects.requireNonNull(em).merge(user);
        log.info("User '{}' updated", user.getEmail());
    }
}
