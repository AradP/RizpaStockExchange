package commands;

import bl.interfaces.ICommand;
import models.Order;
import models.Stock;
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
        stocksInfo.append(selectedStock.getBasicInfo()).append("\n");
        stocksInfo.append("And more info about the orders of this stock:\n");
        final ArrayList<Order> sortedOrders = selectedStock.getOrdersSortedByDate();

        if (sortedOrders.size() == 0) {
            stocksInfo.append("No orders were made on this stock\n");
        } else {
            // Get the basic info about every order
            for (Order order : sortedOrders) {
                stocksInfo.append(order.getBasicInfo());
            }
        }

        return stocksInfo.toString();
    }

    @Override
    public String getCommandName() {
        return "Show A Single stocks";
    }
}
