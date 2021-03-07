package uilayer;

import bl.interfaces.ICommand;
import commands.CommandHandler;
import stocks.StockHandler;

import java.util.List;

public class Main {
    private static final ConsoleIOHandler ConsoleHandler = new ConsoleIOHandler();
    private static final CommandHandler commandHandler = new CommandHandler();
    private static final List<ICommand> commandsList = commandHandler.getCommands();

    public static void main(String[] args) {
        String currentCommand;

        ConsoleHandler.write("Hello!");

        // Start waiting for commands
        while (true) {
            ConsoleHandler.write("Choose A Command:");

            // Show the main commands menu
            showMainCommandsMenu();

            // Get the chosen command from the user input
            currentCommand = ConsoleHandler.read();

            // Validates the chosen command really is presented
            if (validateCommandInput(currentCommand)) {
                final int currentCommandNum = Integer.parseInt(currentCommand) - 1;

                final ICommand command = commandsList.get(currentCommandNum);

                // Map the command to its execution
                switch (currentCommandNum) {
                    // Read System Details File Command
                    case (0): {
                        // Get the file's path
                        ConsoleHandler.write("Enter the file's path (must be xml file):");
                        final String filePath = ConsoleHandler.read();
                        ConsoleHandler.write(command.execute(filePath));
                        break;
                    }
                    // Show all stocks command
                    case (1): {
                        ConsoleHandler.write(command.execute());
                        break;
                    }
                    // Show a single stock command
                    case (2): {
                        if (!StockHandler.getInstance().areStocksLoaded()) {
                            ConsoleHandler.write("You need to load stocks to the system first...");
                            break;
                        }
                        ConsoleHandler.write("Enter stock symbol:");
                        final String stockName = ConsoleHandler.read();
                        ConsoleHandler.write(command.execute(stockName));
                        break;
                    }
                    // Execute exchange process
                    case (3): {
                        // TODO: Omer
                        break;
                    }
                    case (4): {
                        // TODO: Omer
                        break;
                    }
                    // Exit system command
                    case (5): {
                        command.execute();
                        break;
                    }
                    // Save current system data to a file
                    case (6): {
                        ConsoleHandler.write("Enter the path of the destination file:");
                        final String path = ConsoleHandler.read();
                        ConsoleHandler.write(command.execute(path));
                        break;
                    }
                    // Load the system data from a file
                    case (7): {
                        ConsoleHandler.write(command.execute());
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }
    }

    private static void showMainCommandsMenu() {
        for (int commandCounter = 0; commandCounter < commandsList.size(); commandCounter++) {
            ConsoleHandler.write(commandCounter + 1 + ". " + commandsList.get(commandCounter).getCommandName());
        }
    }

    /**
     * Raw validation of the entered command (only if it's a number and is in the list)
     *
     * @param command - the command the validate
     * @return true if it's valid. Else false
     */
    private static boolean validateCommandInput(String command) {
        final int commandNumber;

        try {
            commandNumber = Integer.parseInt(command);
        } catch (NumberFormatException e) {
            ConsoleHandler.write("The commands are represented by their number from the menu. Please enter a number");
            return false;
        }

        if ((commandNumber > commandsList.size()) || (commandNumber <= 0)) {
            ConsoleHandler.write("The number you entered is not in the list. Please try again");
            return false;
        }

        return true;

    }
}
