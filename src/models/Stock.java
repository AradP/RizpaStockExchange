package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class Stock {
    private String symbol;
    private String companyName;
    private double price;
    private ArrayList<Order> orders;

    public Stock(String symbol, String companyName, int price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.orders = new ArrayList<>();
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
     *
     * @return - the information as a string
     */
    public String getBasicInfo() {
        double ordersPeriod = 0;
        final ArrayList<Order> sortedOrders = getOrdersSortedByDate();

        // Calculate orders period of the last month
        for (Order order : sortedOrders) {
            String monthAgoTimestamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now().minusMonths(1));
            if (order.compareByDate(monthAgoTimestamp) >= 0) {
                ordersPeriod += order.getVolume();
            }
            else break;
        }

        return "Symbol: " + symbol + "\n" +
                "Company Name: " + companyName + "\n" +
                "Price: " + price + "\n" +
                "Total orders number: " + orders.size() + "\n" +
                "Last Month Orders Period: " + ordersPeriod + "\n";
    }
}
