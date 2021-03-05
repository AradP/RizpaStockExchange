package stocks.exceptions;

public class CompanyAlreadyExistException extends StockException {
    public CompanyAlreadyExistException() {
        super("Company already have stocks");
    }
}
