package commands;

import bl.interfaces.ICommand;
import models.Stock;
import stocks.StockHandler;
import stocks.StockTransaction;

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
            stocksInfo.append(stock.getBasicInfo()).append("\n");
            final ArrayList<StockTransaction> sortedStockTransactions = stock.getSortedByDateStockTransactions();
            if (sortedStockTransactions.size() > 0) {
                stocksInfo.append("And more info about the transactions of this stock:\n");

                // Get the basic info about every stockTransaction
                for (StockTransaction stockTransaction : sortedStockTransactions) {
                    stocksInfo.append(stockTransaction.getBasicInfo());
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
