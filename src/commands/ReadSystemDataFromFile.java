package commands;

import bl.interfaces.ICommand;
import models.Stock;
import stocks.StockHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadSystemDataFromFile implements ICommand {
    @Override
    public String execute(String... value) {
        try {
            final String filePath = FileIO.getInstance().getFilePath();
            if (filePath == null) {
                return "Please save the system data to a file before this command";
            }

            FileInputStream fis = new FileInputStream(FileIO.getInstance().getFilePath());
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Stock> stocks = (ArrayList<Stock>) ois.readObject();
            StockHandler.getInstance().setStocks(stocks);
            ois.close();

            return "Loaded the data to the system successfully";
        } catch (IOException | ClassNotFoundException e) {
            return "There was a problem while reading the data from the file because of " + e.getMessage();
        }
    }

    @Override
    public String getCommandName() {
        return "Read System Data From a File";
    }
}
