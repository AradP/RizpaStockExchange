package commands;

import bl.interfaces.ICommand;
import models.Stock;
import stocks.StockHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class SaveSystemDataToFile implements ICommand {

    @Override
    public String execute(String... value) {
        // Check that there are stocks loaded
        if (!StockHandler.getInstance().areStocksLoaded()) {
            return "You need to load stocks to the system first...";
        }

        final String filePath = value.clone()[0];

        FileIO.getInstance().setFilePath(filePath);

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Get the stocks list
            ArrayList<Stock> stocks = StockHandler.getInstance().getStocks();

            // Write the stocks to the file
            oos.writeObject(stocks);
            oos.close();
        } catch (IOException e) {
            return "There was a problem while saving the data to the file because of " + e.getMessage();
        }

        return "Saved the current data to " + filePath + " successfully";
    }

    @Override
    public String getCommandName() {
        return "Save System Data To A File";
    }
}
