package commands;

import systemEngine.Main;

public class ExitSystemCommand extends AbstractCommand {
    public ExitSystemCommand() {
    }

    @Override
    public String execute(String value) {
        Main.isSystemRunning = false;

        System.exit(0);

        // TODO: Remove?
        // We will never get here, but it just for compilation.
        return "Goodbye";
    }

    @Override
    public String getName() {
        return "Exit System";
    }
}
