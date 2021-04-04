package exceptions;

public class SymbolDoesntExistException extends StockException {
    /**
     * @param symbol - the problematic symbol
     */
    public SymbolDoesntExistException(final String symbol) {
        super(symbol + " does not exist");
    }
}
