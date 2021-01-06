package pl.poznan.put.controller.user.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    @Override
    public String getMessage() {
        return "User with that email already exists";
    }
}
