package stocks.exceptions;

public class StockSymbolAlreadyExistException extends StockException {
    public StockSymbolAlreadyExistException() {
        super(StockSymbolAlreadyExistException.class.getSimpleName(), "Stock symbol is already exists");
    }
}
