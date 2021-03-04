package commands;

import stocks.Stock;
import stocks.StockHandler;
import stocks.StockTransaction;

import java.util.ArrayList;

public class ShowStocksCommand extends AbstractCommand {
    @Override
    public String execute(String... value) {
        // TODO: Check that there is a valid XML system file loaded

        StringBuilder stocksInfo = new StringBuilder();
        ArrayList<Stock> stocks = StockHandler.getInstance().getStocks();

        // Get the basic info about every stock
        for (Stock stock : stocks) {
            stocksInfo.append("Basic Info is:\n");
            stocksInfo.append(stock.getBasicInfo()).append("\n");
            stocksInfo.append("And more info about the transactions of this stock:\n");
            final ArrayList<StockTransaction> sortedStockTransactions = stock.getSortedByDateStockTransactions();

            // Get the basic info about every stockTransaction
            for (StockTransaction stockTransaction : sortedStockTransactions) {
                stocksInfo.append(stockTransaction.getBasicInfo());
            }
        }

        return stocksInfo.toString();
    }

    @Override
    public String getName() {
        return "Show All Stocks";
    }
}
