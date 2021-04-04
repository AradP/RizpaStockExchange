package exceptions;

public abstract class StockException extends Exception {
    /**
     * @param message - message of the exception
     */
    protected StockException(final String message) {
        super(message);
    }
}
