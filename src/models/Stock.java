package models;

import java.util.ArrayList;
import java.util.Comparator;

public class Stock {
    private String symbol;
    private String companyName;
    private double price;
    private ArrayList<Order> orders;
    private int ordersPeriod;

    public Stock(String symbol, String companyName, int price, int ordersPeriod) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.orders = new ArrayList<>();
        this.ordersPeriod = ordersPeriod;
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

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public int getOrdersPeriod() {
        return this.ordersPeriod;
    }

    public void setOrdersPeriod(final int ordersPeriod) {
        this.ordersPeriod = ordersPeriod;
    }

    /**
     * Returns the orders sorted be date
     *
     * @return the sorted orders
     */
    public ArrayList<Order> getOrdersSortedByDate() {
        ArrayList<Order> sortedOrders = new ArrayList<>(orders);
        sortedOrders.sort(Comparator.comparing(Order::getTimestamp));
        return sortedOrders;

    }

    /**
     * Get basic information about the stock
     * @return - the information as a string
     */
    public String getBasicInfo() {
        return "Symbol: " + symbol + "\n" +
                "Company Name: " + companyName + "\n" +
                "Price: " + price + "\n" +
                "Orders Sum: " + orders.size() + "\n" +
                "Orders Period: " + ordersPeriod + "\n";
    }
}
