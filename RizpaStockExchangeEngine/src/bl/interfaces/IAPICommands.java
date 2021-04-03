package bl.interfaces;

import stocks.exceptions.InvalidSystemDataFile;
import stocks.exceptions.StockException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IAPICommands {
    void loadConfigurationFileByPath(final String xmlFilePath) throws StockException, JAXBException, FileNotFoundException;

    String getAllStocks();

    String getStock(final String symbol) throws StockException;

    String sellLMTOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException;

    String buyLMTOrder(final String symbol, final int numberOfStocks, final double highestPrice) throws StockException;

    String sellFOKOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException;

    String buyFOKOrder(final String symbol, final int numberOfStocks, final double highestPrice) throws StockException;

    String sellIOCOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException;

    String buyIOCOrder(final String symbol, final int numberOfStocks, final double highestPrice) throws StockException;

    String sellMKTOrder(final String symbol, final int numberOfStocks) throws StockException;

    String buyMKTOrder(final String symbol, final int numberOfStocks) throws StockException;

    String getPendingSellOrders(final String symbol);

    String getPendingBuyOrders(final String symbol);

    String getTransactionsHistory(final String symbol);

    void saveDataToFile(final String path) throws IOException;

    void loadDataFromFile(final String path) throws InvalidSystemDataFile, IOException, ClassNotFoundException;

    void exitSystem();
}
