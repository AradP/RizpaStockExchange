package commands;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    private List<AbstractCommand> commands;

    public CommandHandler() {
        this.commands = new ArrayList<>();

        // TODO: Ichsss
        this.commands.add(new ReadSystemDetailsFileCommand());
        this.commands.add(new ShowStocksCommand());
        this.commands.add(new ShowSingleStockCommand());
        this.commands.add(new ExecuteTradeCommand());
        this.commands.add(new ShowAllCommandsCommand());
        this.commands.add(new ExitSystemCommand());

    }

    public String handleCommand(final int commandNum) {
        AbstractCommand command = this.commands.get(commandNum);


        return "hole!";
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
        return this.commands;
    }

    public void setCommands(final List<AbstractCommand> commands) {
        this.commands = commands;
    }

    public String executeCommand(final AbstractCommand command, final String value) {
        return command.execute(value);
    }
}
