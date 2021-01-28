package pl.poznan.put.logic.user.current;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import pl.poznan.put.model.user.User;

@UtilityClass
@Slf4j
public class CurrentUser {
    private final ObjectProperty<User> loggedInUserProperty = new SimpleObjectProperty<>();

    public User getLoggedInUser() {
        return loggedInUserProperty.get();
    }

    public void setLoggedInUser(User user) {
        loggedInUserProperty.set(user);
    }

    static {
        loggedInUserProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) log.info("'{}' logged in", newValue.getEmail());
            else if (oldValue != null) log.info("'{}' logged out", oldValue.getEmail());
        });
    }
}
