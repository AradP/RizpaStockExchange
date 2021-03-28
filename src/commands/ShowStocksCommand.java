package commands;

import bl.interfaces.ICommand;
import models.Order;
import models.Stock;
import models.Transaction;
import stocks.StockHandler;

import java.util.ArrayList;

public class ShowStocksCommand implements ICommand {
    @Override
    public String execute(String... value) {
        // Check that there are stocks loaded
        if (!StockHandler.getInstance().areStocksLoaded()) {
            return "You need to load stocks to the system first...";
        }

        StringBuilder stocksInfo = new StringBuilder();
        ArrayList<Stock> stocks = StockHandler.getInstance().getStocks();

        // Get the basic info about every stock
        for (Stock stock : stocks) {
            stocksInfo.append("Basic Info is:\n");
            stocksInfo.append(stock.toString()).append("\n");
            final ArrayList<Transaction> sortedTransactions = stock.getTransactionsSortedByDate();

            if (sortedTransactions.size() > 0) {
                stocksInfo.append("And more info about the transactions of this stock:\n");

                // Get the basic info about every order
                for (Transaction transaction : sortedTransactions) {
                    stocksInfo.append(transaction.toString());
                }
            }
        }

        return stocksInfo.toString();
    }

    @Override
    public String getCommandName() {
        return "Show All Stocks";
    }
}
