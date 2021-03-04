package commands;

import stocks.Stock;
import stocks.StockHandler;

public class ShowSingleStockCommand extends AbstractCommand {
    public ShowSingleStockCommand() {
    }

    @Override
    //TODO: Throw exception(s) so it would know there is no xml and so ShowStocksCommand won't happen too?
    public String execute(String value) {
        final StockHandler stockHandler = new StockHandler();

        // Check that there is a valid XML system file loaded
        if (!stockHandler.areStocksLoaded()) {
            return "Please load a valid xml file before this command";
        }
        final Stock selectedStock = stockHandler.getStockBySymbol(value);

        if (selectedStock == null) {
            return "Selected stock doesn't exist";
        }

        return selectedStock.getBasicInfo();
    }

    @Override
    public String getName() {
        return "Show A Single stocks.Stock";
    }
}
