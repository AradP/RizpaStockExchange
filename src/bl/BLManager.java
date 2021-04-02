package bl;

import bl.interfaces.IAPICommands;
import enums.OrderType;
import models.Order;
import models.Stock;
import models.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import stocks.StockHandler;
import stocks.exceptions.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
    public void loadConfigurationFileByPath(final String xmlFilePath) throws StockException {
        final File systemDetailsFile = new File(xmlFilePath);

        // Validates this really is a xml file
        if (!getFileExtension(systemDetailsFile).equals(".xml")) {
            throw new InvalidSystemDataFile("the given file is not a xml file");
        }

        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new InvalidSystemDataFile(e.getMessage());
        }

        Document doc = null;
        try {
            doc = dBuilder.parse(systemDetailsFile);
        } catch (SAXException | IOException e) {
            throw new InvalidSystemDataFile(e.getMessage());

        }

        doc.getDocumentElement().normalize();
        final NodeList nList = doc.getElementsByTagName("rse-stock");
        final ArrayList<Stock> newStocks = new ArrayList<Stock>();

        // Get the information about each stock
        for (int temp = 0; temp < nList.getLength(); temp++) {
            final Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element eElement = (Element) nNode;

                final String tempStockSymbol = eElement.getElementsByTagName("rse-symbol").item(0).getTextContent();

                // Validate the stock contains only letters and only in English
                if (!isAlpha(tempStockSymbol)) {
                    throw new InvalidSystemDataFile("Stock symbol must contain only English letters");
                }

                int tempStockPrice = 0;

                try {
                    tempStockPrice = Integer.parseInt(eElement.getElementsByTagName("rse-price").item(0).getTextContent());
                } catch (NumberFormatException e) {
                    throw new InvalidSystemDataFile("Price must be a number");
                }

                if (tempStockPrice <= 0) {
                    throw new InvalidSystemDataFile("Price must be a positive number");
                }

                final String tempCompnanyName = eElement.getElementsByTagName("rse-company-name").item(0).getTextContent();

                if (tempCompnanyName.isEmpty()) {
                    throw new InvalidSystemDataFile("Company name can't be empty");
                }

                if (tempCompnanyName.startsWith(" ") || tempCompnanyName.endsWith(" ")) {
                    throw new InvalidSystemDataFile("Company name can't start or end with a space");
                }

                Stock stock = new Stock(tempStockSymbol.toUpperCase(),
                        tempCompnanyName,
                        tempStockPrice);

                newStocks.add(stock);
            }
        }

        // Validates the stocks entered
        validateSystemFile(newStocks);

        // We can successfully update the stocks in our system
        StockHandler.getInstance().setStocks(newStocks);
    }

    @Override
    public String getAllStocks() {
        final StringBuilder stocksInfo = new StringBuilder();
        final ArrayList<Stock> stocks = StockHandler.getInstance().getStocks();

        // Get the basic info about every stock
        for (Stock stock : stocks) {
            stocksInfo.append("Basic Info of this stock is:\n");
            stocksInfo.append(stock.toString()).append("\n");
        }

        return stocksInfo.toString();
    }

    @Override
    public String getStock(final String symbol) throws StockException {
        final Stock selectedStock = StockHandler.getInstance().getStockBySymbol(symbol);

        if (selectedStock == null) {
            throw new SymbolDoesntExistException(symbol);
        }

        final StringBuilder stocksInfo = new StringBuilder();

        // Get the basic info
        stocksInfo.append("Basic Info of this stock is:\n");
        stocksInfo.append(selectedStock.toString()).append("\n");
        stocksInfo.append("And more info about the transactions of this stock:\n");
        final ArrayList<Transaction> sortedTransactions = selectedStock.getTransactionsSortedByDate();

        if (sortedTransactions.size() == 0) {
            stocksInfo.append("No transactions were made on this stock\n");
        } else {
            // Get the basic info about every transaction
            for (Transaction transaction : sortedTransactions) {
                stocksInfo.append(transaction.toString());
                stocksInfo.append("\n");
            }
        }

        return stocksInfo.toString();
    }

    @Override
    public String sellLMTOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockHandler.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks, lowestPrice, OrderType.LMT);
                if (StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(true);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue = returnValue.concat(transaction.toString()) + "\n";
                    }

                } else {
                    returnValue = "The order has been added to the book" + "\n";
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String buyLMTOrder(final String symbol, int numberOfStocks, final double highestPrice) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockHandler.getInstance().getStockBySymbol(symbol).CreateBuyOrder(numberOfStocks, highestPrice, OrderType.LMT);
                if (StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(false);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue = returnValue.concat(transaction.toString()) + "\n";
                    }

                } else {
                    returnValue = "The order has been added to the book" + "\n";
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String sellFOKOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                if (!StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransactionFOK(true, numberOfStocks, lowestPrice)) {
                    returnValue = "It is not possible to add this order." + "\n";
                } else {
                    StockHandler.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks, lowestPrice, OrderType.FOK);
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(true);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue = returnValue.concat(transaction.toString()) + "\n";
                    }
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String buyFOKOrder(final String symbol, final int numberOfStocks, final double highestPrice) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                if (!StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransactionFOK(false, numberOfStocks, highestPrice)) {
                    returnValue = "It is not possible to add this order." + "\n";
                } else {
                    StockHandler.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks, highestPrice, OrderType.FOK);
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(false);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue = returnValue.concat(transaction.toString()) + "\n";
                    }
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String sellIOCOrder(final String symbol, final int numberOfStocks, final double lowestPrice) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                int isItPossibleToMakeATransactionIOC = StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransactionIOC(true, numberOfStocks, lowestPrice);
                if (isItPossibleToMakeATransactionIOC == -1) {
                    returnValue = "It is not possible to add this order." + "\n";
                } else {
                    StockHandler.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks - isItPossibleToMakeATransactionIOC, lowestPrice, OrderType.IOC);
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(true);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue = returnValue.concat(transaction.toString()) + "\n";
                    }
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String buyIOCOrder(final String symbol, final int numberOfStocks, final double highestPrice) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                int isItPossibleToMakeATransactionIOC = StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransactionIOC(false, numberOfStocks, highestPrice);
                if (isItPossibleToMakeATransactionIOC == -1) {
                    returnValue = "It is not possible to add this order." + "\n";
                } else {
                    StockHandler.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks - isItPossibleToMakeATransactionIOC, highestPrice, OrderType.IOC);
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(false);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue = returnValue.concat(transaction.toString()) + "\n";
                    }
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String sellMKTOrder(final String symbol, final int numberOfStocks) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockHandler.getInstance().getStockBySymbol(symbol).CreateSellOrderMKT(numberOfStocks);
                if (StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(true);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue = returnValue.concat(transaction.toString()) + "\n";
                    }

                } else {
                    returnValue = "The order has been added to the book" + "\n";
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String buyMKTOrder(final String symbol, final int numberOfStocks) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockHandler.getInstance().getStockBySymbol(symbol).CreateBuyOrderMKT(numberOfStocks);
                if (StockHandler.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockHandler.getInstance().getStockBySymbol(symbol).makeATransaction(false);

                    returnValue = "The order has been executed successfully and transactions had been made:" + "\n";
                    for (Transaction transaction : newTransactions) {
                        returnValue = returnValue.concat(transaction.toString()) + "\n";
                    }

                } else {
                    returnValue = "The order has been added to the book" + "\n";
                }
            }
        } catch (StockException e) {
            throw e;
        }

        return returnValue + "\n";
    }

    @Override
    public String getPendingSellOrders(final String symbol) {
        String returnValue = "";
        List<Order> orders = StockHandler.getInstance().getPendingSellOrder(symbol);

        if (orders.size() == 0) {
            returnValue = "Zero sell orders are pending" + "\n" +
                    "Total sell orders volume: 0" + "\n";
        } else if (orders.size() == 1) {
            returnValue = "One sell order is pending:" + "\n"
                    + orders.get(0).toString() + "\n" +
                    "Total sell orders volume: " + orders.get(0).getVolume();
        } else {
            int totalOrderPrice = 0;
            returnValue = orders.size() + " sell orders are pending:" + "\n";
            for (Order order : orders) {
                returnValue = returnValue.concat(order.toString()) + "\n";
                totalOrderPrice += order.getVolume();
            }

            returnValue = returnValue.concat("Total sell orders volume: " + totalOrderPrice);
        }
        return returnValue + "\n";
    }

    @Override
    public String getPendingBuyOrders(final String symbol) {
        String returnValue = "";
        List<Order> orders = StockHandler.getInstance().getPendingBuyOrder(symbol);

        if (orders.size() == 0) {
            returnValue = "Zero buy orders are pending" + "\n" +
                    "Total buy orders volume: 0";
        } else if (orders.size() == 1) {
            returnValue = "One buy order is pending:" + "\n"
                    + orders.get(0).toString() + "\n" +
                    "Total buy orders volume: " + orders.get(0).getVolume();
        } else {
            int totalOrderPrice = 0;
            returnValue = orders.size() + " buy orders are pending:" + "\n";
            for (Order order : orders) {
                returnValue = returnValue.concat(order.toString()) + " \n";
                totalOrderPrice += order.getVolume();
            }

            returnValue = returnValue.concat("Total buy orders volume: " + totalOrderPrice);
        }
        return returnValue + "\n";
    }

    @Override
    public String getTransactionsHistory(final String symbol) {
        String returnValue = "";
        final List<Transaction> transactions = StockHandler.getInstance().getTransactionsHistory(symbol);

        int totalOrderPrice = 0;
        returnValue = "Number of Executed Transactions: " + transactions.size() + "\n";

        for (Transaction transaction : transactions) {
            returnValue = returnValue.concat(transaction.toString()) + "\n";
            totalOrderPrice += transaction.getVolume();
        }

        returnValue = returnValue.concat("Total Executed Transactions Volume: " + totalOrderPrice);
        return returnValue + "\n";
    }

    @Override
    public void saveDataToFile(String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        // Get the stocks list
        ArrayList<Stock> stocks = StockHandler.getInstance().getStocks();

        // Write the stocks to the file
        oos.writeObject(stocks);
        oos.close();
    }

    @Override
    public void loadDataFromFile(String path) throws InvalidSystemDataFile, IOException, ClassNotFoundException {
        if (path == null) {
            throw new InvalidSystemDataFile("it doesn't exist");
        }

        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Stock> stocks = (ArrayList<Stock>) ois.readObject();
        StockHandler.getInstance().setStocks(stocks);
        ois.close();
    }

    private boolean verifySellBuyExecution(String symbol, int numberOfStocks) throws SymbolDoesntExistException, StocksNumberIsntValidException {
        if (!StockHandler.getInstance().isSymbolExists(symbol)) {
            throw new SymbolDoesntExistException(symbol);
        } else if (numberOfStocks <= 0) {
            throw new StocksNumberIsntValidException();
        }
        return true;
    }

    /**
     * Validates the entered stocks are unique (symbol and company name)
     *
     * @param stocks - the stocks to validate
     * @throws StockSymbolAlreadyExistException - if symbol already exists
     * @throws CompanyAlreadyExistException     - if company already has stocks
     */
    private void validateSystemFile(final ArrayList<Stock> stocks) throws StockSymbolAlreadyExistException, CompanyAlreadyExistException {
        // We don't have to run for each stock, because we already do it in the inner for
        for (int stockCounter = 0; stockCounter < stocks.size(); stockCounter++) {
            for (int tempStockCounter = stockCounter + 1; tempStockCounter < stocks.size(); tempStockCounter++) {

                // Check the symbol is unique
                if (stocks.get(tempStockCounter).getSymbol().toUpperCase(Locale.ROOT).equals(stocks.get(stockCounter).getSymbol().toUpperCase())) {
                    throw new StockSymbolAlreadyExistException(stocks.get(tempStockCounter).getSymbol());
                }

                // Check the company name is unique
                if (stocks.get(tempStockCounter).getCompanyName().equals(stocks.get(stockCounter).getCompanyName())) {
                    throw new CompanyAlreadyExistException(stocks.get(stockCounter).getCompanyName());
                }
            }
        }
    }

    /**
     * Get the file extension
     *
     * @param file - the file
     * @return - the extension (".xml" for example)
     */
    private String getFileExtension(final File file) {
        final String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");

        if (lastIndexOf == -1) {
            return ""; // empty extension
        }

        return name.substring(lastIndexOf);
    }

    /**
     * Checks if the given string contain only letters
     *
     * @param name - the string to check
     * @return - true if contains only letters, else false
     */
    private boolean isAlpha(final String name) {
        return name.matches("[a-zA-Z]+");
    }
}
