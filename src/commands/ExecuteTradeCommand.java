package commands;

import bl.interfaces.ICommand;

public class ExecuteTradeCommand implements ICommand {
    @Override
    public String execute(String... value) {
        return null;
    }

    @Override
    public String getCommandName() {
        return "Execute Trade";
    }
}
