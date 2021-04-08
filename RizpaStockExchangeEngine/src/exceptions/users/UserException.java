package exceptions.users;

public abstract class UserException extends Exception {
    /**
     * @param message - message of the exception
     */
    protected UserException(final String message) {
        super(message);
    }
}
