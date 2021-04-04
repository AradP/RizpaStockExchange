package exceptions;

public class StockSymbolAlreadyExistException extends StockException {
    /**
     * @param symbol - the problematic symbol
     */
    public StockSymbolAlreadyExistException(final String symbol) {
        super("Stock symbol " + symbol + " already exists");
    }
}
