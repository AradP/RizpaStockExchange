package bl.interfaces;

import exceptions.InvalidSystemDataFile;
import exceptions.stocks.StockException;
import exceptions.users.UserAlreadyExistsException;
import models.User;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IAPICommands {
    void loadConfigurationFileByPath(final String xmlFilePath) throws StockException, JAXBException, FileNotFoundException, UserAlreadyExistsException;

    String getAllStocks();

    String getStock(final String symbol) throws StockException;

    String sellLMTOrder(final String symbol, final int numberOfStocks, final double lowestPrice, User seller) throws StockException;

    String buyLMTOrder(final String symbol, final int numberOfStocks, final double highestPrice, User buyer) throws StockException;

    String sellFOKOrder(final String symbol, final int numberOfStocks, final double lowestPrice, User seller) throws StockException;

    String buyFOKOrder(final String symbol, final int numberOfStocks, final double highestPrice, User buyer) throws StockException;

    String sellIOCOrder(final String symbol, final int numberOfStocks, final double lowestPrice, User seller) throws StockException;

    String buyIOCOrder(final String symbol, final int numberOfStocks, final double highestPrice, User buyer) throws StockException;

    String sellMKTOrder(final String symbol, final int numberOfStocks, User seller) throws StockException;

    String buyMKTOrder(final String symbol, final int numberOfStocks, User buyer) throws StockException;

    String getPendingSellOrders(final String symbol);

    String getPendingBuyOrders(final String symbol);

    String getTransactionsHistory(final String symbol);

    void saveDataToFile(final String path) throws IOException;

    void loadDataFromFile(final String path) throws InvalidSystemDataFile, IOException, ClassNotFoundException;

    void exitSystem();
}
