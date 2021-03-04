package stocks.exceptions;

import java.util.logging.Logger;

public abstract class StockException extends Throwable {
    private static final Logger logger = Logger.getLogger(StockException.class.getName());

    /**
     * Logs the exception
     * @param classException - class type of the exception
     * @param message - message of the exception
     */
    public StockException(final String classException, final String message) {
        logger.severe(message);
    }
}
