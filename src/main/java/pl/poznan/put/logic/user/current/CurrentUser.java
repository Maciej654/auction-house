package pl.poznan.put.logic.user.current;

import javafx.beans.property.ObjectProperty;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import pl.poznan.put.model.user.User;

@UtilityClass
public class CurrentUser {
    @Getter
    private ObjectProperty<User> loggedInUserProperty;

    public User getLoggedInUser() {
        return loggedInUserProperty.get();
    }

    public void setLoggedInUser(User user) {
        loggedInUserProperty.set(user);
    }
}
