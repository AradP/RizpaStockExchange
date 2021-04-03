package apigateway;

import bl.BLManager;
import bl.interfaces.IAPICommands;
import models.Stock;
import stocks.StockHandler;
import stocks.exceptions.InvalidSystemDataFile;
import stocks.exceptions.StockException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    public void loadConfigurationFileByPath(final String xmlFilePath) throws StockException, JAXBException, FileNotFoundException {
        blManager.loadConfigurationFileByPath(xmlFilePath);
    }

    @Override
    public String getAllStocks() {
        return blManager.getAllStocks();
    }

    @Override
    public String getStock(final String symbol) throws StockException {
        return blManager.getStock(symbol);
    }

    @Override
    public String sellLMTOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException {
        try {
            return blManager.sellLMTOrder(symbol, numberOfStocks, lowestPrice);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String buyLMTOrder(final String symbol, final int numberOfStocks, final double highestPrice) throws StockException {
        try {
            return blManager.buyLMTOrder(symbol, numberOfStocks, highestPrice);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String sellFOKOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException {
        try {
            return blManager.sellFOKOrder(symbol, numberOfStocks, lowestPrice);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String buyFOKOrder(final String symbol, final int numberOfStocks, final double highestPrice) throws StockException {
        try {
            return blManager.buyFOKOrder(symbol, numberOfStocks, highestPrice);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String sellIOCOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException {
        try {
            return blManager.sellIOCOrder(symbol, numberOfStocks, lowestPrice);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String buyIOCOrder(final String symbol, final int numberOfStocks, final double highestPrice) throws StockException {
        try {
            return blManager.buyIOCOrder(symbol, numberOfStocks, highestPrice);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String sellMKTOrder(final String symbol, final int numberOfStocks) throws StockException {
        try {
            return blManager.sellMKTOrder(symbol, numberOfStocks);
        } catch (StockException e) {
            throw e;
        }
    }

    @Override
    public String buyMKTOrder(final String symbol, final int numberOfStocks) throws StockException {
        try {
            return blManager.buyMKTOrder(symbol, numberOfStocks);
        } catch (StockException e) {
            throw e;
        }
    }

    public String AllOrdersAndTransactions() {
        String returnValue = "";

        for (final Stock stock : StockHandler.getInstance().getStocks()) {
            returnValue = returnValue.concat(stock.getSymbol() + ":" + "\n");
            returnValue = returnValue.concat(BLManager.getInstance().getPendingBuyOrders(stock.getSymbol()));
            returnValue = returnValue.concat("\n");

            returnValue = returnValue.concat(BLManager.getInstance().getPendingSellOrders(stock.getSymbol()));
            returnValue = returnValue.concat("\n");

            returnValue = returnValue.concat(BLManager.getInstance().getTransactionsHistory(stock.getSymbol()));
            returnValue = returnValue.concat("\n");
        }

        return returnValue;
    }

    @Override
    public String getPendingSellOrders(final String symbol) {
        return blManager.getPendingSellOrders(symbol);
    }

    @Override
    public String getPendingBuyOrders(final String symbol) {
        return blManager.getPendingBuyOrders(symbol);
    }

    @Override
    public String getTransactionsHistory(final String symbol) {
        return blManager.getTransactionsHistory(symbol);
    }

    @Override
    public void saveDataToFile(final String path) throws IOException {
        blManager.saveDataToFile(path);
    }

    @Override
    public void loadDataFromFile(final String path) throws InvalidSystemDataFile, IOException, ClassNotFoundException {
        blManager.loadDataFromFile(path);
    }

    @Override
    public void exitSystem() {
        blManager.exitSystem();
    }
}
