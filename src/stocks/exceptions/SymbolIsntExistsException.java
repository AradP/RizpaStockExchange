package stocks.exceptions;

public class SymbolIsntExistsException extends StockException {
    public SymbolIsntExistsException(final String symbol) {
        super(symbol + " is not exists");
    }
}
