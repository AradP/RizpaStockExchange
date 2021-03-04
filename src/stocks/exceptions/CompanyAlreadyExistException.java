package stocks.exceptions;

public class CompanyAlreadyExistException extends StockException {
    public CompanyAlreadyExistException() {
        super(CompanyAlreadyExistException.class.getSimpleName(), "Company already have stocks");
    }
}
