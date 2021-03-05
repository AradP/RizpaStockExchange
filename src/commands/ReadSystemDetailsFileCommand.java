package commands;

import bl.interfaces.ICommand;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import models.Stock;
import stocks.StockHandler;
import stocks.exceptions.CompanyAlreadyExistException;
import stocks.exceptions.StockSymbolAlreadyExistException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ReadSystemDetailsFileCommand implements ICommand {

    @Override
    public String execute(String... value) {
        final File systemDetailsFile = new File(value.clone()[0]);

        // Validates this really is a xml file
        if (!getFileExtension(systemDetailsFile).equals(".xml")) {
            return "Please insert a valid xml file";
        }

        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return "Couldn't load the file because " + e.getMessage();
        }

        Document doc = null;
        try {
            doc = dBuilder.parse(systemDetailsFile);
        } catch (SAXException | IOException e) {
            return "Couldn't parse the file because of " + e.getMessage();

        }

        doc.getDocumentElement().normalize();
        final NodeList nList = doc.getElementsByTagName("rse-stock");

        final ArrayList<Stock> newStocks = new ArrayList<Stock>();

        for (int temp = 0; temp < nList.getLength(); temp++) {
            final Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element eElement = (Element) nNode;

                Stock stock = null;
                try {
                    stock = new Stock(eElement.getElementsByTagName("rse-symbol").item(0).getTextContent().toUpperCase(),
                            eElement.getElementsByTagName("rse-company-name").item(0).getTextContent(),
                            Integer.parseInt(eElement.getElementsByTagName("rse-price").item(0).getTextContent()), 0);
                } catch (NumberFormatException e) {
                    return "The price must be a number";
                }

                newStocks.add(stock);
            }
        }

        // Validates the stocks entered
        try {
            validateSystemFile(newStocks);
        } catch (StockSymbolAlreadyExistException | CompanyAlreadyExistException e) {
            return e.getMessage();
        }

        // We can successfully update the stocks in our system
        StockHandler.getInstance().setStocks(newStocks);

        return "Successfully updated the stocks in the system";
    }

    @Override
    public String getCommandName() {
        return "Read System Details File";
    }

    /**
     * Validates the entered stocks are unique (symbol and company name)
     *
     * @param stocks - the stocks to validate
     * @throws StockSymbolAlreadyExistException - if symbol already exists
     * @throws CompanyAlreadyExistException     - if company already has stocks
     */
    private void validateSystemFile(ArrayList<Stock> stocks) throws StockSymbolAlreadyExistException, CompanyAlreadyExistException {
        // We don't have to run for each stock, because we already do it in the inner for
        for (int stockCounter = 0; stockCounter < stocks.size(); stockCounter++) {
            for (int tempStockCounter = stockCounter + 1; tempStockCounter < stocks.size(); tempStockCounter++) {

                // Check the symbol is unique
                if (stocks.get(tempStockCounter).getSymbol().toUpperCase(Locale.ROOT).equals(stocks.get(stockCounter).getSymbol().toUpperCase())) {
                    throw new StockSymbolAlreadyExistException(stocks.get(tempStockCounter).getSymbol());
                }

                // Check the company name is unique
                if (stocks.get(tempStockCounter).getCompanyName().equals(stocks.get(stockCounter).getCompanyName())) {
                    throw new CompanyAlreadyExistException(stocks.get(stockCounter).getCompanyName());
                }
            }
        }
    }

    /**
     * Get the file extension
     *
     * @param file - the file
     * @return - the extension (".xml" for example)
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
