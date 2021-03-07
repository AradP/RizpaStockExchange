package commands;

import bl.interfaces.ICommand;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    private List<ICommand> commands;

    public CommandHandler() {
        this.commands = new ArrayList<>();

        this.commands.add(new ReadSystemDetailsFileCommand());
        this.commands.add(new ShowStocksCommand());
        this.commands.add(new ShowSingleStockCommand());
        this.commands.add(new ExecuteTradeCommand());
        this.commands.add(new ShowAllCommandsCommand());
        this.commands.add(new ExitSystemCommand());
        this.commands.add(new SaveSystemDataToFile());
        this.commands.add(new ReadSystemDataFromFile());

    }

    /**
     * Converts the given stExecute Tradering that represents a command to the actual command
     *
     * @param command - the given command string to convert
     * @return the actual command
     */
    private ICommand convertStringToActualCommand(final String command) {
        return new ExitSystemCommand();
    }

    public List<ICommand> getCommands() {
        return this.commands;
    }

    public void setCommands(final List<ICommand> commands) {
        this.commands = commands;
    }

    public String executeCommand(final ICommand command, final String value) {
        return command.execute(value);
    }
}
