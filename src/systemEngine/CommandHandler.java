package systemEngine;

import commands.AbstractCommand;
import commands.CommandExecutor;
import commands.ExitSystemCommand;

import java.util.List;

public class CommandHandler {
    private static final CommandExecutor commandExecutor = new CommandExecutor();

    public String handleCommand(final String command) {
        if (!isCommandValid(command)) {
            return "";
        }

        if (command.equals("exit")) {
         return commandExecutor.executeCommand(new ExitSystemCommand(), "abc");
        }

        return "hole!";
    }

    /**
     * Validates the given command
     * @param command - the command to check
     * @return true if valid, else false
     */
    private boolean isCommandValid(final String command) {
//        if (command.isBlank() || !commandExecutor.getCommands().contains(command)) {
//            return false;
//        }

        return true;
    }

    /**
     * Converts the given stExecute Tradering that represents a command to the actual command
     * @param command - the given command string to convert
     * @return the actual command
     */
    private AbstractCommand convertStringToActualCommand(final String command) {
        return new ExitSystemCommand();
    }

    public List<AbstractCommand> getCommands() {
        return commandExecutor.getCommands();
    }
}
