package exceptions.stocks;

public class CompanyAlreadyExistException extends StockException {
    public CompanyAlreadyExistException(final String companyName) {
        super("Company " + companyName + " already have stocks");
    }
}
