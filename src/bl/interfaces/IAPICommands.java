package bl.interfaces;

import java.util.List;

//TODO: Should find better name!
public interface IAPICommands {
    boolean loadConfigurationFileByPath(String xmlFilePath);

    String getAllStocks();

    String getStock(String symbol);

    boolean sellLimitOrder(String symbol, int numberOfStocks, double lowestPrice);

    boolean buyLimitOrder(String symbol, int numberOfStocks, double highestPrice);

    List<String> getOrderSellBook(String symbol);

    List<String> getOrderBuyBook(String symbol);

    List<String> getTradesHistory(String symbol);
}
