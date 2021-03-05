package stocks.exceptions;

public class StockSymbolAlreadyExistException extends StockException {
    public StockSymbolAlreadyExistException(final String symbol) {
        super("Stock symbol " + symbol + " already exists");
    }
}
