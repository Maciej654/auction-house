package pl.poznan.put.controller.user.crud.update;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import pl.poznan.put.controller.user.crud.UserCrudController;
import pl.poznan.put.controller.user.validation.UserPropertyValidator;
import pl.poznan.put.controller.user.validation.UserUpdatePasswordValidator;
import pl.poznan.put.model.user.User;

import java.util.Objects;

@Slf4j
public class UserUpdateController extends UserCrudController {
    private User user;

    @Override
    protected UserPropertyValidator<String> getPasswordValidator() {
        return new UserUpdatePasswordValidator();
    }

    @Override
    public void setUser(User user) {
        super.setUser(user);
        this.user = user;
    }

    @Override
    public User getUser() {
        val user = super.getUser();
        if (Objects.nonNull(this.user) && StringUtils.isBlank(passwordField.getText())) {
            user.setHash(this.user.getHash());
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
