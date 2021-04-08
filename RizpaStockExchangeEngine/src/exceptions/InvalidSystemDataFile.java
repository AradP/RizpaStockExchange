package exceptions;

import exceptions.stocks.StockException;

public class InvalidSystemDataFile extends StockException {
    /**
     * @param message - message of the exception
     */
    public InvalidSystemDataFile(String message) {
        super("System data file is not valid because: " + message);
    }
}
