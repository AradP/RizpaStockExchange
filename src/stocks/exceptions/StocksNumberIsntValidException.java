package stocks.exceptions;

public class StocksNumberIsntValidException extends StockException{
    public StocksNumberIsntValidException() {
        super("Stocks number is not valid");
    }
}
