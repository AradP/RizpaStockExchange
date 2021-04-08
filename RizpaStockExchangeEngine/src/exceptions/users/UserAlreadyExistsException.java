package exceptions.users;

public class UserAlreadyExistsException extends UserException {
    /**
     * @param message - message of the exception
     */
    public UserAlreadyExistsException(final String message) {
        super("User " + message + " Already Exists");
    }
}
