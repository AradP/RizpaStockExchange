package systemEngine;

import commands.AbstractCommand;
import ui.ConsoleIOHandler;
import ui.IIOHandler;

import java.util.List;

public class Main {
    private static final IIOHandler ioHandler = new ConsoleIOHandler();
    private static final CommandHandler commandHandler = new CommandHandler();

    public static void main(String[] args) {
        String currentCommandNum;
        String currentCommandResult;

        // Start waiting for commands
        while (true) {
            ioHandler.write("Hello! Choose A Command:");

            // Show the main commands menu
            showMainCommandsMenu();
            // Get the chosen command from the user input
            currentCommandNum = ioHandler.read();

            // Validates the chosen command really is presented
            if (validateCommandInput(currentCommandNum)) {
                // Map the command to its execution
                switch (currentCommandNum) {

                }
                // If it's a command that needs another value:
                ioHandler.write("Great, now you need to choose:");
                currentCommandResult = commandHandler.handleCommand(currentCommandNum);
                ioHandler.write(currentCommandResult);
            }
        }
    }

    private static void showMainCommandsMenu() {
        List<AbstractCommand> commandList = commandHandler.getCommands();
        int commandCounter = 1;

        for (AbstractCommand command : commandList) {
            ioHandler.write( commandCounter + ". " + command.getName());
            commandCounter++;
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
            ioHandler.write("The commands are represented by their number from the menu. Please enter a number");
            return false;
        }

        if ((commandNumber >= commandHandler.getCommands().size()) || (commandNumber <= 0)) {
            ioHandler.write("The number you entered is not in the list. Please try again");
            return false;
        }

        return true;

    }
}
