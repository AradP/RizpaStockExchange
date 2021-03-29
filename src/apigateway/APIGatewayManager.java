package apigateway;

import bl.BLManager;
import bl.interfaces.IAPICommands;
import models.Stock;
import stocks.StockHandler;
import stocks.exceptions.StockException;

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

    private final BLManager blManager = BLManager.getInstance();

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
    public String sellLMTOrder(String symbol, int numberOfStocks, double lowestPrice) throws StockException {
        try {
            return blManager.sellLMTOrder(symbol, numberOfStocks, lowestPrice);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String buyLMTOrder(String symbol, int numberOfStocks, double highestPrice) throws StockException {
        try {
            return blManager.buyLMTOrder(symbol, numberOfStocks, highestPrice);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String sellFOKOrder(String symbol, int numberOfStocks, double lowestPrice) throws StockException {
        try {
            return blManager.sellFOKOrder(symbol, numberOfStocks, lowestPrice);
        } catch (StockException e) {
            throw e;
        }    }

    @Override
    public String buyFOKOrder(String symbol, int numberOfStocks, double highestPrice) throws StockException {
        try {
            return blManager.buyFOKOrder(symbol, numberOfStocks, highestPrice);
        } catch (StockException e) {
            throw e;
        }    }

    @Override
    public String sellIOCOrder(String symbol, int numberOfStocks, double lowestPrice) throws StockException {
        try {
            return blManager.sellIOCOrder(symbol, numberOfStocks, lowestPrice);
        } catch (StockException e) {
            throw e;
        }    }

    @Override
    public String buyIOCOrder(String symbol, int numberOfStocks, double highestPrice) throws StockException {
        try {
            return blManager.buyIOCOrder(symbol, numberOfStocks, highestPrice);
        } catch (StockException e) {
            throw e;
        }    }

    @Override
    public String sellMKTOrder(String symbol, int numberOfStocks) throws StockException {
        try {
            return blManager.sellMKTOrder(symbol, numberOfStocks);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String buyMKTOrder(String symbol, int numberOfStocks) throws StockException {
        try {
            return blManager.buyMKTOrder(symbol, numberOfStocks);
        } catch (StockException e) {
            throw e;
        }
    }

    public String AllOrdersAndTransactions() {
        String returnValue = "";

        for (Stock stock : StockHandler.getInstance().getStocks()) {
            returnValue = returnValue.concat(stock.getSymbol() + ":" + "\n");
            returnValue = returnValue.concat(BLManager.getInstance().getPendingBuyOrder(stock.getSymbol()));
            returnValue = returnValue.concat("\n");

            returnValue = returnValue.concat(BLManager.getInstance().getPendingSellOrder(stock.getSymbol()));
            returnValue = returnValue.concat("\n");

            returnValue = returnValue.concat(BLManager.getInstance().getTransactionsHistory(stock.getSymbol()));
            returnValue = returnValue.concat("\n");
        }

        return returnValue;
    }

    @Override
    public String getPendingSellOrder(String symbol) {
        return blManager.getPendingSellOrder(symbol);
    }

    @Override
    public String getPendingBuyOrder(String symbol) {
        return blManager.getPendingBuyOrder(symbol);
    }

    @Override
    public String getTransactionsHistory(String symbol) {
        return blManager.getTransactionsHistory(symbol);
    }

    @Override
    public String saveDataToFile(String path) {
        return blManager.saveDataToFile(path);
    }
}
