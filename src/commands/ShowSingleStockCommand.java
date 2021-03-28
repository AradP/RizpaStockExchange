package commands;

import bl.interfaces.ICommand;
import models.Order;
import models.Stock;
import models.Transaction;
import stocks.StockHandler;

import java.util.ArrayList;

public class ShowSingleStockCommand implements ICommand {
    @Override
    public String execute(String... value) {
        // Check that there is a valid XML system file loaded
        if (!StockHandler.getInstance().areStocksLoaded()) {
            return "Please load a valid xml file before this command";
        }

        final Stock selectedStock = StockHandler.getInstance().getStockBySymbol(value.clone()[0]);

        if (selectedStock == null) {
            return "Selected stock doesn't exist";
        }

        StringBuilder stocksInfo = new StringBuilder();
        ArrayList<Stock> stocks = StockHandler.getInstance().getStocks();

        // Get the basic info
        stocksInfo.append("Basic Info is:\n");
        stocksInfo.append(selectedStock.toString()).append("\n");
        stocksInfo.append("And more info about the transactions of this stock:\n");
        final ArrayList<Transaction> sortedTransactions = selectedStock.getTransactionsSortedByDate();

        if (sortedTransactions.size() == 0) {
            stocksInfo.append("No transactions were made on this stock\n");
        } else {
            // Get the basic info about every transaction
            for (Transaction transaction : sortedTransactions) {
                stocksInfo.append(transaction.toString());
            }
        }

        return stocksInfo.toString();
    }

    @Override
    public String getCommandName() {
        return "Show A Single stocks";
    }
}
