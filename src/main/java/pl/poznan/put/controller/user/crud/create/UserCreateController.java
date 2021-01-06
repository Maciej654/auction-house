package pl.poznan.put.controller.user.crud.create;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.controller.user.crud.UserCrudController;
import pl.poznan.put.controller.user.exception.EmailAlreadyInUseException;
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
    protected void crudOperation(User user) throws EmailAlreadyInUseException {
        log.info("create");
        Objects.requireNonNull(em);
        val other = em.find(User.class, user.getEmail());
        if (other == null) em.persist(user);
        else throw new EmailAlreadyInUseException();
        log.info("User '{}' created", user.getEmail());
    }
}
