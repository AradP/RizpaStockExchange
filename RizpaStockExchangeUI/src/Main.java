import apigateway.APIGatewayManager;
import bl.BLManager;
import bl.StockManager;
import exceptions.InvalidSystemDataFile;
import exceptions.StockException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static final ConsoleIOHandler ConsoleHandler = new ConsoleIOHandler();

    public static void main(String[] args) {
        int currentCommand;

        ConsoleHandler.write("Hello!");

        // Start waiting for commands
        while (true) {
            ConsoleHandler.write("Choose A Command:");

            // Show the main commands menu
            showMainCommandsMenu();

            // Get the chosen command from the user input
            currentCommand = ConsoleHandler.readInt() - 1;

            // Map the command to its execution
            switch (currentCommand) {
                // Read System Details File Command
                case (0): {
                    // Get the file's path
                    ConsoleHandler.write("Enter the file's path (must be xml file):");
                    final String filePath = ConsoleHandler.read();

                    try {
                        APIGatewayManager.getInstance().loadConfigurationFileByPath(filePath);
                        ConsoleHandler.write("Successfully updated the stocks in the system");
                    } catch (StockException | FileNotFoundException | JAXBException e) {
                        ConsoleHandler.write(e.getMessage());
                    }

                    break;
                }
                // Show all stocks command
                case (1): {
                    if (!StockManager.getInstance().areStocksLoaded()) {
                        ConsoleHandler.write("You need to load stocks to the system first...");
                        break;
                    }

                    ConsoleHandler.write(APIGatewayManager.getInstance().getAllStocks());
                    break;
                }
                // Show a single stock command
                case (2): {
                    if (!StockManager.getInstance().areStocksLoaded()) {
                        ConsoleHandler.write("You need to load stocks to the system first...");
                        break;
                    }

                    ConsoleHandler.write("Enter stock symbol:");
                    final String stockName = ConsoleHandler.read();
                    try {
                        ConsoleHandler.write(APIGatewayManager.getInstance().getStock(stockName));
                    } catch (StockException stockException) {
                        ConsoleHandler.write(stockException.getMessage());
                    }

                    break;
                }
                // Execute exchange process
                case (3): {
                    if (!StockManager.getInstance().areStocksLoaded()) {
                        ConsoleHandler.write("You need to load stocks to the system first...");
                        break;
                    }

                    ConsoleHandler.write("For sell press 1, buy press 2, to return to the menu press 9:");
                    int isBuyOrder = ConsoleHandler.readInt();


                    while (isBuyOrder != 1 && isBuyOrder != 2 && isBuyOrder != 9) {
                        ConsoleHandler.write("For sell press 1, buy press 2, to return to the menu press 9:");
                        isBuyOrder = ConsoleHandler.readInt();
                    }
                    if (isBuyOrder == 9) {
                        break;
                    }

                    ConsoleHandler.write("Enter stock symbol:");
                    final String stockName = ConsoleHandler.read();

                    ConsoleHandler.write("For LMT press 1, MKT press 2, to return to the menu press 9:");
                    int orderTypeSymbol = ConsoleHandler.readInt();

                    while (orderTypeSymbol != 1 && orderTypeSymbol != 2 && orderTypeSymbol != 9) {
                        ConsoleHandler.write("For LMT press 1, MKT press 2, to return to the menu press 9:");
                        orderTypeSymbol = ConsoleHandler.readInt();
                    }
                    if (orderTypeSymbol == 9) {
                        break;
                    }

                    int exchangeRate = 0;


                    // LMT
                    if (orderTypeSymbol == 1) {
                        ConsoleHandler.write("Enter exchange rate (price):");
                        exchangeRate = ConsoleHandler.readInt();

                        while (exchangeRate < 1) {
                            ConsoleHandler.write("Enter correct exchange rate (price):");
                            exchangeRate = ConsoleHandler.readInt();
                        }
                    }

                    ConsoleHandler.write("Enter amount of stocks:");
                    int amountOfStocks = ConsoleHandler.readInt();

                    while (amountOfStocks < 1) {
                        ConsoleHandler.write("Enter correct amount of stocks:");
                        amountOfStocks = ConsoleHandler.readInt();
                    }

                    try {
                        // LMT
                        if (orderTypeSymbol == 1) {
                            String returnValue = isBuyOrder == 1 ? APIGatewayManager.getInstance().sellLMTOrder(stockName, amountOfStocks, exchangeRate) :
                                    APIGatewayManager.getInstance().buyLMTOrder(stockName, amountOfStocks, exchangeRate);
                            ConsoleHandler.write(returnValue);
                        }
                        //MKT
                        else if (orderTypeSymbol == 2) {
                            String returnValue = isBuyOrder == 1 ? APIGatewayManager.getInstance().sellMKTOrder(stockName, amountOfStocks) :
                                    APIGatewayManager.getInstance().buyMKTOrder(stockName, amountOfStocks);
                            ConsoleHandler.write(returnValue);
                        }

                    } catch (StockException e) {
                        ConsoleHandler.write(e.getMessage());
                    }

                    break;
                }
                case (4): {
                    if (!StockManager.getInstance().areStocksLoaded()) {
                        ConsoleHandler.write("You need to load stocks to the system first...");
                        break;
                    }

                    ConsoleHandler.write(APIGatewayManager.getInstance().AllOrdersAndTransactions());
                    break;
                }
                // Exit system command
                case (5): {
                    System.exit(0);

                    // No need for a break, because anyway we shut down the program
                }
                // Save current system data to a file
                case (6): {
                    ConsoleHandler.write("Enter the path of the destination file:");
                    final String path = ConsoleHandler.read();

                    try {
                        APIGatewayManager.getInstance().saveDataToFile(path);
                        ConsoleHandler.write("Saved the current data successfully");
                    } catch (final IOException ioException) {
                        ConsoleHandler.write("There was a problem while saving the data to the file " +
                                "because of " + ioException.getMessage());
                    }

                    break;
                }
                // Load the system data from a file
                case (7): {
                    ConsoleHandler.write("Enter the file's path:");
                    final String filePath = ConsoleHandler.read();

                    try {
                        BLManager.getInstance().loadDataFromFile(filePath);
                        ConsoleHandler.write("Loaded the data to the system successfully");
                    } catch (final IOException | ClassNotFoundException | InvalidSystemDataFile e) {
                        ConsoleHandler.write("There was a problem while reading the data because: " + e.getMessage());
                    }
                    break;
                }
                default: {
                    // If we got to here then the user entered a number of a command that doesn't exist
                    ConsoleHandler.write("Please choose a number from the list");

                    break;
                }
            }
        }
    }

    private static void showMainCommandsMenu() {
        ConsoleHandler.write("1. Read System Details File\n" +
                "2. Show All Stocks\n" +
                "3. Show A Single stock\n" +
                "4. Execute Trade\n" +
                "5. Show All Trade Commands\n" +
                "6. Exit System\n" +
                "7. Save System Data To A File\n" +
                "8. Load System Data From a Previously Saved File");
    }
}
