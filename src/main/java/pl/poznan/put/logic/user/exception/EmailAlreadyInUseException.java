package pl.poznan.put.logic.user.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    @Override
    public String getMessage() {
        return "User with that email already exists";
    }
}
