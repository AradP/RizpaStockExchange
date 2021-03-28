package bl;

import bl.interfaces.IAPICommands;
import enums.OrderType;
import models.Order;
import models.Transaction;
import stocks.StockHandler;
import stocks.exceptions.StockException;
import stocks.exceptions.StocksNumberIsntValidException;
import stocks.exceptions.SymbolIsntExistsException;

import java.util.ArrayList;
import java.util.List;

public final class BLManager implements IAPICommands {

    //region Lazy Singleton

    private static BLManager instance;

    private BLManager() {
    }

    public static BLManager getInstance() {
        if (instance == null) {
            instance = new BLManager();
        }
        return instance;
    }

    //endregion

    @Override
    public boolean loadConfigurationFileByPath(String xmlFilePath) {
        return true;
    }

    @Override
    public String getAllStocks() {
        return null;
    }

    @Override
    public String getStock(String symbol) {
        return null;
    }

    @Override
    public String sellLimitOrder(String symbol, int numberOfStocks, double lowestPrice) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockHandler.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks, lowestPrice, OrderType.LMT);
                if (StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(true);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue=returnValue.concat(transaction.toString());
                    }

                } else {
                    returnValue = "The order has been added to the book";
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String buyLimitOrder(String symbol, int numberOfStocks, double highestPrice) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockHandler.getInstance().getStockBySymbol(symbol).CreateBuyOrder(numberOfStocks, highestPrice, OrderType.LMT);
                if (StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(false);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue=returnValue.concat(transaction.toString());
                    }

                } else {
                    returnValue = "The order has been added to the book";
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue+ "\n";
    }

    @Override
    public String getPendingSellOrder(String symbol) {
        String returnValue = "";
        List<Order> orders = StockHandler.getInstance().getPendingSellOrder(symbol);

        if (orders.size() == 0) {
            returnValue = "Zero sell orders is pending" + "\n" +
                    "Total sell orders volume: 0";
        } else if (orders.size() == 1) {
            returnValue = "One sell order is pending:" + "\n"
                    + orders.get(0).toString() + "\n" +
                    "Total sell orders volume: " + orders.get(0).getVolume();
        } else {
            int totalOrderPrice = 0;
            returnValue = orders.size() + " sell orders are pending:" + "\n";
            for (Order order : orders) {
                returnValue=returnValue.concat(order.toString());
                totalOrderPrice += order.getVolume();
            }

            returnValue=returnValue.concat("Total sell orders volume: " + totalOrderPrice);
        }
        return returnValue+ "\n";
    }

    @Override
    public String getPendingBuyOrder(String symbol) {
        String returnValue = "";
        List<Order> orders = StockHandler.getInstance().getPendingBuyOrder(symbol);

        if (orders.size() == 0) {
            returnValue = "Zero buy orders is pending" + "\n" +
                    "Total buy orders volume: 0";
        } else if (orders.size() == 1) {
            returnValue = "One buy order is pending:" + "\n"
                    + orders.get(0).toString() + "\n" +
                    "Total buy orders volume: " + orders.get(0).getVolume();
        } else {
            int totalOrderPrice = 0;
            returnValue = orders.size() + " buy orders are pending:" + "\n";
            for (Order order : orders) {
                returnValue=returnValue.concat(order.toString());
                totalOrderPrice += order.getVolume();
            }

            returnValue=returnValue.concat("Total buy orders volume: " + totalOrderPrice);
        }
        return returnValue+ "\n";
    }

    @Override
    public String getTransactionsHistory(String symbol) {
        String returnValue = "";
        List<Transaction> transactions = StockHandler.getInstance().getTransactionsHistory(symbol);

        if (transactions.size() == 0) {
            returnValue = "Zero transaction is pending" + "\n" +
                    "Total transaction volume: 0";
        } else if (transactions.size() == 1) {
            returnValue = "One transaction is pending:" + "\n"
                    + transactions.get(0).toString() + "\n" +
                    "Total transactions volume: " + transactions.get(0).getVolume();
        } else {
            int totalOrderPrice = 0;
            returnValue = transactions.size() + " transactions are pending:" + "\n";
            for (Transaction transaction : transactions) {
                returnValue = returnValue.concat(transaction.toString());
                totalOrderPrice += transaction.getVolume();
            }

            returnValue = returnValue.concat("Total transactions volume: " + totalOrderPrice);
        }
        return returnValue+ "\n";
    }

    @Override
    public String saveDataToFile(String path) {
        return null;
    }

    private boolean verifySellBuyExecution(String symbol, int numberOfStocks) throws SymbolIsntExistsException, StocksNumberIsntValidException {
        if (!StockHandler.getInstance().isSymbolExists(symbol)) {
            throw new SymbolIsntExistsException(symbol);
        } else if (numberOfStocks <= 0) {
            throw new StocksNumberIsntValidException();
        }
        return true;
    }
}
