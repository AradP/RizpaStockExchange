package commands;

import bl.interfaces.ICommand;

public class ShowAllCommandsCommand implements ICommand {
    @Override
    public String execute(String... value) {
        return null;
    }

    @Override
    public String getCommandName() {
        return "Show All Trade Commands";
    }
}
