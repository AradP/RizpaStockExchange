package models;

import stocks.StockTransaction;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Stock {
    private String symbol;
    private String companyName;
    private double price;
    private ArrayList<StockTransaction> stockTransactions;
    private int transactionsPeriod;

    public Stock(String symbol, String companyName, int price, int transactionsPeriod) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.stockTransactions = new ArrayList<>();
        this.transactionsPeriod = transactionsPeriod;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(final String symbol) {
        if (!symbol.isEmpty() | symbol.equals("")) {
            this.symbol = symbol;
        }
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(final String companyName) {
        if (!companyName.isEmpty()) {
            this.companyName = companyName;
        }
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(final double price) {
        if (price > 0) {
            this.price = price;
        }
    }

    public ArrayList<StockTransaction> getStockTransactions() {
        return stockTransactions;
    }

    public int getTransactionsPeriod() {
        return this.transactionsPeriod;
    }

    public void setTransactionsPeriod(final int transactionsPeriod) {
        this.transactionsPeriod = transactionsPeriod;
    }

    /**
     * Returns the stockTransactions arraylist sorted be date
     *
     * @return the sorted stockTransactions
     */
    public ArrayList<StockTransaction> getSortedByDateStockTransactions() {
        return this.stockTransactions
                .stream()
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public String getBasicInfo() {
        return "Symbol: " + symbol + "\n" +
                "Company Name: " + companyName + "\n" +
                "Price: " + price + "\n" +
                "Transactions Sum: " + stockTransactions.size() + "\n" +
                "Transactions Period: " + transactionsPeriod + "\n";
    }
}
