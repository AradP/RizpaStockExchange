package commands;

import bl.interfaces.ICommand;

public class ExitSystemCommand implements ICommand {

    @Override
    public String execute(String... value) {
        System.exit(0);

        // TODO: Remove?
        // We will never get here, but it just for compilation.
        return "Goodbye";
    }

    @Override
    public String getCommandName() {
        return "Exit System";
    }
}
