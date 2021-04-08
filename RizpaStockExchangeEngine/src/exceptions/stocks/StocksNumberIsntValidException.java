package exceptions.stocks;

public class StocksNumberIsntValidException extends StockException {
    public StocksNumberIsntValidException() {
        super("Stocks number is not valid");
    }
}
