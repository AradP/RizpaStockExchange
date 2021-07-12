package bl;

import models.Order;
import models.Stock;
import models.Transaction;
import exceptions.stocks.CompanyAlreadyExistException;
import exceptions.stocks.StockSymbolAlreadyExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockManager {
    // static variable single_instance of type Singleton
    private static StockManager single_instance = null;

    private ArrayList<Stock> stocks;

    private StockManager() {
        this.stocks = new ArrayList<>();
    }

    public synchronized ArrayList<Stock> getStocks() {
        return stocks;
    }

    public synchronized ArrayList<Stock> getStocksWithOrdersPeriod() {
        stocks.forEach(stock -> stock.setOrdersPeriod(stock.getOrderPeriod()));
        return stocks;
    }

    public synchronized void setStocks(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }

    // static method to create instance of Singleton class
    public static StockManager getInstance() {
        if (single_instance == null)
            single_instance = new StockManager();

        return single_instance;
    }

    public synchronized void addStocks(List<Stock> newStocks) {
        this.stocks.addAll(newStocks);
    }

    /**
     * Adds a stock to the stocks list if not exist
     *
     * @param stock - the stock to add
     * @throws StockSymbolAlreadyExistException - thrown if stock symbol already exits
     */
    public synchronized void addStock(Stock stock) throws StockSymbolAlreadyExistException, CompanyAlreadyExistException {
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

    public synchronized Stock getStockBySymbol(final String stockName) {
        Optional<Stock> tempStock = stocks.stream().filter(stock -> stock.getSymbol().equalsIgnoreCase(stockName)).findAny();
        return tempStock.orElse(null);
    }

    public synchronized Stock getStockBySymbolWithOrdersPeriod(final String stockName) {
        Optional<Stock> tempStock = stocks.stream().filter(stock -> stock.getSymbol().equalsIgnoreCase(stockName)).findAny();
        if (!tempStock.isPresent()) {
            return null;
        } else {
            Stock s = tempStock.get();
            s.setOrdersPeriod(s.getOrderPeriod());
            return s;
        }
    }

    public synchronized Stock getStockBySymbolInList(final String stockName, final List<Stock> tempStocks) {
        Optional<Stock> tempStock = tempStocks.stream().filter(stock -> stock.getSymbol().equalsIgnoreCase(stockName)).findAny();
        return tempStock.orElse(null);
    }

    public synchronized Stock getStockByCompany(final String companyName) {
        Optional<Stock> tempStock = stocks.stream().filter(stock -> stock.getCompanyName().equals(companyName)).findAny();
        return tempStock.orElse(null);
    }

    public synchronized boolean isSymbolExists(final String symbol) {
        return stocks.stream().anyMatch(stock -> stock.getSymbol().equalsIgnoreCase(symbol));
    }

    public synchronized List<Order> getPendingSellOrder(final String symbol) {
        return getStockBySymbol(symbol).getPendingSellOrders();
    }

    public synchronized List<Order> getPendingBuyOrder(final String symbol) {
        return getStockBySymbol(symbol).getPendingBuyOrders();
    }

    public synchronized List<Transaction> getTransactionsHistory(final String symbol) {
        return getStockBySymbol(symbol).getTransactionsSortedByDate();
    }

    /**
     * We need to check that there is a valid xml file loaded in our system
     *
     * @return - true if there are stocks loaded, else false
     */
    public synchronized boolean areStocksLoaded() {
        return stocks.size() > 0;
    }
}
