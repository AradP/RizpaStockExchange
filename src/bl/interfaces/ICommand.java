package bl.interfaces;

public interface ICommand {
    String execute(String... value);

    String getCommandName();
}
