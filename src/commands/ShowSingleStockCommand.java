package commands;

import bl.interfaces.ICommand;
import models.Stock;
import stocks.StockHandler;

import java.util.Arrays;

public class ShowSingleStockCommand implements ICommand {
    @Override
    //TODO: Throw exception(s) so it would know there is no xml and so ShowStocksCommand won't happen too?
    public String execute(String... value) {
        // Check that there is a valid XML system file loaded
        if (!StockHandler.getInstance().areStocksLoaded()) {
            return "Please load a valid xml file before this command";
        }
        final Stock selectedStock = StockHandler.getInstance().getStockBySymbol(Arrays.toString(value));

        if (selectedStock == null) {
            return "Selected stock doesn't exist";
        }

        return selectedStock.getBasicInfo();
    }

    @Override
    public String getCommandName() {
        return "Show A Single stocks";
    }
}
