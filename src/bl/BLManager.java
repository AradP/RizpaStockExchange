package bl;

import bl.interfaces.IAPICommands;

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
    public boolean sellLimitOrder(String symbol, int numberOfStocks, double lowestPrice) {
        return false;
    }

    @Override
    public boolean buyLimitOrder(String symbol, int numberOfStocks, double highestPrice) {
        return false;
    }

    @Override
    public List<String> getOrderSellBook(String symbol) {
        return null;
    }

    @Override
    public List<String> getOrderBuyBook(String symbol) {
        return null;
    }

    @Override
    public List<String> getTradesHistory(String symbol) {
        return null;
    }
}
