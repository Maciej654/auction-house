package pl.poznan.put.controller.user.crud.create;

import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.controller.user.crud.UserCrudController;
import pl.poznan.put.controller.user.validation.UserCreatePasswordValidator;
import pl.poznan.put.controller.user.validation.UserPropertyValidator;
import pl.poznan.put.model.user.User;

import java.util.Objects;

@Slf4j
public class UserCreateController extends UserCrudController {
    @Override
    protected UserPropertyValidator<String> getPasswordValidator() {
        return new UserCreatePasswordValidator();
    }

    @Override
    protected void crudOperation(User user) {
        Objects.requireNonNull(em).persist(user);
        log.info("User '{}' created", user.getEmail());
    }
}
