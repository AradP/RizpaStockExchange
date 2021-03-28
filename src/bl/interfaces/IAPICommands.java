package bl.interfaces;

import stocks.exceptions.StockException;

import java.util.List;

//TODO: Should find better name!
public interface IAPICommands {
    boolean loadConfigurationFileByPath(String xmlFilePath);

    String getAllStocks();

    String getStock(String symbol);

    String sellLimitOrder(String symbol, int numberOfStocks, double lowestPrice) throws StockException;

    String buyLimitOrder(String symbol, int numberOfStocks, double highestPrice) throws StockException;
    String sellMKTOrder(String symbol, int numberOfStocks) throws StockException;

    String buyMKTOrder(String symbol, int numberOfStocks) throws StockException;

    String getPendingSellOrder(String symbol);

    String getPendingBuyOrder(String symbol);

    String getTransactionsHistory(String symbol);

    String saveDataToFile(String path);
}
