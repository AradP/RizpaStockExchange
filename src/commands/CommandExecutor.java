package commands;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {
    private List<AbstractCommand> commands;

    public CommandExecutor() {
        this.commands = new ArrayList<>();
        this.commands.add(new ExecuteTradeCommand());
        this.commands.add(new ExitSystemCommand());
        this.commands.add(new ReadSystemDetailsFileCommand());
        this.commands.add(new ShowStocksCommand());
        this.commands.add(new ShowAllCommandsCommand());
        this.commands.add(new ShowSingleStockCommand());
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
