package bl;

import bl.interfaces.IAPICommands;
import enums.OrderType;
import exceptions.InvalidSystemDataFile;
import exceptions.stocks.*;
import exceptions.users.UserAlreadyExistsException;
import models.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

public final class BLManager implements IAPICommands {

    private final static String JAXB_XML_PKG_NAME = "models";
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
    public void loadConfigurationFileForUser(final File systemDetailsFile, User user) throws StockException, JAXBException, FileNotFoundException {

        // Validates this really is a xml file
        if (!getFileExtension(systemDetailsFile).equals(".xml")) {
            throw new InvalidSystemDataFile("the given file is not a xml file");
        }

        final InputStream inputStream = new FileInputStream(systemDetailsFile);
        final RizpaStockExchangeDescriptor rseDescriptor = deserializeFrom(inputStream);

        // Stocks Validation
        final ArrayList<Stock> newStocks = new ArrayList<>();
        final ArrayList<Stock> stocksFromFile = new ArrayList<>();

        for (final RseStock currRseStock : rseDescriptor.getRseStocks().getRseStock()) {
            validateRSEStock(currRseStock);

            final Stock stock = new Stock(currRseStock.getRseSymbol().toUpperCase(),
                    currRseStock.getRseCompanyName(),
                    currRseStock.getRsePrice());

            // Skip if the stock symbol already exist in the system
            if (!StockManager.getInstance().isSymbolExists(stock.getSymbol())) {
                newStocks.add(stock);
            }

            stocksFromFile.add(stock);
        }

        // Validates the stocks entered
        validateStocksInSystemFile(newStocks);

        // Holdings Validation
        HashMap<Stock, Integer> newHoldings = getRSEHoldings(stocksFromFile, rseDescriptor.getRseHoldings());

        // We can successfully update the stocks in our system
        StockManager.getInstance().addStocks(newStocks);

        // We can successfully update the holdings for the user
        user.setHoldings(newHoldings);
    }

    @Override
    public String getAllStocks() {
        final StringBuilder stocksInfo = new StringBuilder();
        final ArrayList<Stock> stocks = StockManager.getInstance().getStocks();

        // Get the basic info about every stock
        for (final Stock stock : stocks) {
            stocksInfo.append("Basic Info of this stock is:\n");
            stocksInfo.append(stock.toString()).append("\n");
        }

        return stocksInfo.toString();
    }

    @Override
    public String getStock(final String symbol) throws StockException {
        final Stock selectedStock = StockManager.getInstance().getStockBySymbol(symbol);

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
            for (final Transaction transaction : sortedTransactions) {
                stocksInfo.append(transaction.toString());
                stocksInfo.append("\n");
            }
        }

        return stocksInfo.toString();
    }

    @Override
    public String sellLMTOrder(final String symbol, final int numberOfStocks, final double lowestPrice, User seller) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockManager.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks, lowestPrice, OrderType.LMT, seller);
                if (StockManager.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockManager.getInstance().getStockBySymbol(symbol).makeATransaction(true);

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
    public String buyLMTOrder(final String symbol, int numberOfStocks, final double highestPrice, User buyer) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockManager.getInstance().getStockBySymbol(symbol).CreateBuyOrder(numberOfStocks, highestPrice, OrderType.LMT, buyer);
                if (StockManager.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockManager.getInstance().getStockBySymbol(symbol).makeATransaction(false);

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
    public String sellFOKOrder(final String symbol, final int numberOfStocks, final double lowestPrice, User seller) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                if (!StockManager.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransactionFOK(true, numberOfStocks, lowestPrice)) {
                    returnValue = "It is not possible to add this order." + "\n";
                } else {
                    StockManager.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks, lowestPrice, OrderType.FOK, seller);
                    newTransactions = StockManager.getInstance().getStockBySymbol(symbol).makeATransaction(true);

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
    public String buyFOKOrder(final String symbol, final int numberOfStocks, final double highestPrice, User buyer) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                if (!StockManager.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransactionFOK(false, numberOfStocks, highestPrice)) {
                    returnValue = "It is not possible to add this order." + "\n";
                } else {
                    StockManager.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks, highestPrice, OrderType.FOK, buyer);
                    newTransactions = StockManager.getInstance().getStockBySymbol(symbol).makeATransaction(false);

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
    public String sellIOCOrder(final String symbol, final int numberOfStocks, final double lowestPrice, User seller) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                int isItPossibleToMakeATransactionIOC = StockManager.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransactionIOC(true, numberOfStocks, lowestPrice);
                if (isItPossibleToMakeATransactionIOC == -1) {
                    returnValue = "It is not possible to add this order." + "\n";
                } else {
                    StockManager.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks - isItPossibleToMakeATransactionIOC, lowestPrice, OrderType.IOC, seller);
                    newTransactions = StockManager.getInstance().getStockBySymbol(symbol).makeATransaction(true);

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
    public String buyIOCOrder(final String symbol, final int numberOfStocks, final double highestPrice, User buyer) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                int isItPossibleToMakeATransactionIOC = StockManager.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransactionIOC(false, numberOfStocks, highestPrice);
                if (isItPossibleToMakeATransactionIOC == -1) {
                    returnValue = "It is not possible to add this order." + "\n";
                } else {
                    StockManager.getInstance().getStockBySymbol(symbol).CreateSellOrder(numberOfStocks - isItPossibleToMakeATransactionIOC, highestPrice, OrderType.IOC, buyer);
                    newTransactions = StockManager.getInstance().getStockBySymbol(symbol).makeATransaction(false);

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
    public String sellMKTOrder(final String symbol, final int numberOfStocks, User seller) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockManager.getInstance().getStockBySymbol(symbol).CreateSellOrderMKT(numberOfStocks, seller);
                if (StockManager.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockManager.getInstance().getStockBySymbol(symbol).makeATransaction(true);

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
    public String buyMKTOrder(final String symbol, final int numberOfStocks, User buyer) throws StockException {
        String returnValue = "";

        try {
            ArrayList<Transaction> newTransactions;
            if (verifySellBuyExecution(symbol, numberOfStocks)) {
                StockManager.getInstance().getStockBySymbol(symbol).CreateBuyOrderMKT(numberOfStocks, buyer);
                if (StockManager.getInstance().getStockBySymbol(symbol).IsItPossibleToMakeATransaction()) {
                    newTransactions = StockManager.getInstance().getStockBySymbol(symbol).makeATransaction(false);

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
        List<Order> orders = StockManager.getInstance().getPendingSellOrder(symbol);

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
        List<Order> orders = StockManager.getInstance().getPendingBuyOrder(symbol);

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
        final List<Transaction> transactions = StockManager.getInstance().getTransactionsHistory(symbol);

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
    public void saveDataToFile(final String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        // Get the stocks list
        ArrayList<Stock> stocks = StockManager.getInstance().getStocks();

        // Write the stocks to the file
        oos.writeObject(stocks);
        oos.close();
    }

    @Override
    public void loadDataFromFile(final String path) throws InvalidSystemDataFile, IOException, ClassNotFoundException {
        if (path == null || !(new File(path).exists())) {
            throw new InvalidSystemDataFile("it doesn't exist");
        }

        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Stock> stocks = (ArrayList<Stock>) ois.readObject();
        StockManager.getInstance().setStocks(stocks);
        ois.close();
    }

    @Override
    public void exitSystem() {
        System.exit(0);
    }

    private boolean verifySellBuyExecution(final String symbol, int numberOfStocks) throws SymbolDoesntExistException, StocksNumberIsntValidException {
        if (!StockManager.getInstance().isSymbolExists(symbol)) {
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
    private void validateStocksInSystemFile(final ArrayList<Stock> stocks) throws StockSymbolAlreadyExistException, CompanyAlreadyExistException {
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

    private void validateUsersInSystemFile(final ArrayList<User> users) throws UserAlreadyExistsException, SymbolDoesntExistException, InvalidSystemDataFile {
        for (int userCounter = 0; userCounter < users.size(); userCounter++) {
            for (int tempUserCounter = userCounter + 1; tempUserCounter < users.size(); tempUserCounter++) {

                // Check the username is unique
                if (users.get(tempUserCounter).getName().equals(users.get(userCounter).getName())) {
                    throw new UserAlreadyExistsException(users.get(tempUserCounter).getName());
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

    private RizpaStockExchangeDescriptor deserializeFrom(final InputStream in) throws JAXBException {
        final JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PKG_NAME);

        final Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }

    /**
     * Checks whether an rseStock is corrupted or not
     *
     * @param rseStock - the rseStock to check
     * @throws InvalidSystemDataFile - if the stock is invalid
     */
    private void validateRSEStock(final RseStock rseStock) throws InvalidSystemDataFile {
        // Validate the stock contains only letters and only in English
        if (!isAlpha(rseStock.getRseSymbol())) {
            throw new InvalidSystemDataFile("Stock symbol must contain only English letters");
        }

        if (rseStock.getRsePrice() <= 0) {
            throw new InvalidSystemDataFile("Price must be a positive number");
        }

        final String currCompanyName = rseStock.getRseCompanyName();
        if (currCompanyName.isEmpty()) {
            throw new InvalidSystemDataFile("Company name can't be empty");
        }

        if (currCompanyName.startsWith(" ") || currCompanyName.endsWith(" ")) {
            throw new InvalidSystemDataFile("Company name can't start or end with a space");
        }
    }

    private HashMap<Stock, Integer> getRSEHoldings(final List<Stock> newStocks, RseHoldings rseHoldings) throws InvalidSystemDataFile, SymbolDoesntExistException {
        HashMap<Stock, Integer> tmpHoldings = new HashMap<>();

        for (RseItem rseItem : rseHoldings.getRseItem()) {
            Stock currStock = StockManager.getInstance().getStockBySymbolInList(rseItem.getSymbol(), newStocks);
            // Check that the stock actually exist in the new stocks list
            if (currStock == null) {
                throw new SymbolDoesntExistException(rseItem.getSymbol());
            }

            // Check that all the holdings' quantities are not negative
            if (rseItem.getQuantity() < 0) {
                throw new InvalidSystemDataFile("Stock quantity must be a positive number");
            }

            tmpHoldings.put(currStock, rseItem.getQuantity());
        }

        return tmpHoldings;
    }
}