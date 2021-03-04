package apigateway;

import bl.BLManager;
import bl.interfaces.IAPICommands;

import java.util.List;

public final class APIGatewayManager implements IAPICommands {

    //region Lazy Singleton

    private static APIGatewayManager instance;

    private APIGatewayManager() {
    }

    public static APIGatewayManager getInstance() {
        if (instance == null) {
            instance = new APIGatewayManager();
        }
        return instance;
    }

    //endregion

    private BLManager blManager = BLManager.getInstance();

    @Override
    public boolean loadConfigurationFileByPath(String xmlFilePath) {
        return blManager.loadConfigurationFileByPath(xmlFilePath);
    }

    @Override
    public String getAllStocks() {
        return blManager.getAllStocks();
    }

    @Override
    public String getStock(String symbol) {
        return blManager.getStock(symbol);
    }

    @Override
    public boolean sellLimitOrder(String symbol, int numberOfStocks, double lowestPrice) {
        return blManager.sellLimitOrder(symbol,numberOfStocks,lowestPrice);
    }

    @Override
    public boolean buyLimitOrder(String symbol, int numberOfStocks, double highestPrice) {
        return blManager.buyLimitOrder(symbol,numberOfStocks,highestPrice);
    }

    @Override
    public List<String> getOrderSellBook(String symbol) {
        return blManager.getOrderSellBook(symbol);
    }

    @Override
    public List<String> getOrderBuyBook(String symbol) {
        return blManager.getOrderBuyBook(symbol);
    }

    @Override
    public List<String> getTradesHistory(String symbol) {
        return blManager.getTradesHistory(symbol);
    }
}
