package commands;

// TODO: Change to interface instead of abstract class?
public abstract class AbstractCommand {
    public abstract String execute(String... value);
    public abstract String getName();
}
