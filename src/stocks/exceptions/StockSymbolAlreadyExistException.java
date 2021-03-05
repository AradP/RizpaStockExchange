package stocks.exceptions;

public class StockSymbolAlreadyExistException extends StockException {
    public StockSymbolAlreadyExistException() {
        super("Stock symbol already exists");
    }
}
