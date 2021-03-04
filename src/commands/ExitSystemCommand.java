package commands;

public class ExitSystemCommand extends AbstractCommand {

    @Override
    public String execute(String... value) {
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
