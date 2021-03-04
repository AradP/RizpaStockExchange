package commands;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import stocks.Stock;
import stocks.StockHandler;
import stocks.exceptions.CompanyAlreadyExistException;
import stocks.exceptions.StockSymbolAlreadyExistException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

public class ReadSystemDetailsFileCommand extends AbstractCommand {
    private static final Logger logger = Logger.getLogger(ReadSystemDetailsFileCommand.class.getName());
    private static final StockHandler stockHandler = new StockHandler();

    @Override
    public String execute(String... value) {
        // TODO: Check that it's really an xml file (.xml)
        final File systemDetailsFile = new File(Arrays.toString(value));
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
        logger.info("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("rse-stock");

        ArrayList<Stock> newStocks = new ArrayList<Stock>();

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            logger.info("Current Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                logger.info("Current Stock: " + eElement.getTagName());

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

        try {
            validateSystemFile(newStocks);
        } catch (StockSymbolAlreadyExistException | CompanyAlreadyExistException e) {
            return e.getMessage();
        }

        // We can successfully update the stocks in our system
        stockHandler.setStocks(newStocks);

        return "Successfully updated the stocks in the system";
    }

    @Override
    public String getName() {
        return "Read System Details File";
    }

    /**
     * Validates the enters stocks are unique (symbol and company name)
     * @param stocks - the stocks to validate
     * @throws StockSymbolAlreadyExistException - if symbol already exists
     * @throws CompanyAlreadyExistException - if company already has stocks
     */
    private void validateSystemFile(ArrayList<Stock> stocks) throws StockSymbolAlreadyExistException, CompanyAlreadyExistException {
        // We don't have to run for each stock, because we already do it in the inner for
        for (int stockCounter = 0; stockCounter < stocks.size(); stockCounter++) {
            for (Stock stock : stocks) {

                // Check the symbol is unique
                if (stock.getSymbol().toUpperCase(Locale.ROOT).equals(stocks.get(stockCounter).getSymbol().toUpperCase())) {
                    throw new StockSymbolAlreadyExistException();
                }

                // Check the company name is unique
                if (stock.getCompanyName().equals(stocks.get(stockCounter).getCompanyName())) {
                    throw new CompanyAlreadyExistException();
                }
            }
        }
    }
}
