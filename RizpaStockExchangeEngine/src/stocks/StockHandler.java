package stocks;

import models.Order;
import models.Stock;
import models.Transaction;
import stocks.exceptions.CompanyAlreadyExistException;
import stocks.exceptions.StockSymbolAlreadyExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockHandler {
    // static variable single_instance of type Singleton
    private static StockHandler single_instance = null;

    private ArrayList<Stock> stocks;

    public StockHandler() {
        this.stocks = new ArrayList<>();
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }

    // static method to create instance of Singleton class
    public static StockHandler getInstance() {
        if (single_instance == null)
            single_instance = new StockHandler();

        return single_instance;
    }

    /**
     * Adds a stock to the stocks list if not exist
     *
     * @param stock - the stock to add
     * @throws StockSymbolAlreadyExistException - thrown if stock symbol already exits
     */
    public void addStock(Stock stock) throws StockSymbolAlreadyExistException, CompanyAlreadyExistException {
        // Check that the stock symbol doesn't already exist
        if (this.getStockBySymbol(stock.getSymbol()) != null) {
            throw new StockSymbolAlreadyExistException(stock.getSymbol());
        }

        // Check that the company doesn't already have stocks
        if (this.getStockByCompany(stock.getCompanyName()) != null) {
            throw new CompanyAlreadyExistException(stock.getCompanyName());
        }

        this.stocks.add(stock);
    }

    public Stock getStockBySymbol(String stockName) {
        Optional<Stock> tempStock = stocks.stream().filter(stock -> stock.getSymbol().equalsIgnoreCase(stockName)).findAny();
        return tempStock.orElse(null);

    }

    public Stock getStockByCompany(String companyName) {
        Optional<Stock> tempStock = stocks.stream().filter(stock -> stock.getCompanyName().equals(companyName)).findAny();
        return tempStock.orElse(null);
    }

    public boolean isSymbolExists(String symbol) {
        return stocks.stream().anyMatch(stock -> stock.getSymbol().equalsIgnoreCase(symbol));
    }

    public List<Order> getPendingSellOrder(String symbol) {
        return getStockBySymbol(symbol).getPendingSellOrders();
    }

    public List<Order> getPendingBuyOrder(String symbol) {
        return getStockBySymbol(symbol).getPendingBuyOrders();
    }

    public List<Transaction> getTransactionsHistory(String symbol) {
        return getStockBySymbol(symbol).getTransactionsSortedByDate();
    }

    /**
     * We need to check that there is a valid xml file loaded in our system
     *
     * @return - true if there are stocks loaded, else false
     */
    public boolean areStocksLoaded() {
        return stocks.size() > 0;
    }
}
