package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private String symbol;
    private String timestamp;
    private int count;
    private double exchangeRate;
    private double volume;

    public Order(String symbol, int count, double exchangeRate) {
        this.symbol = symbol;
        this.count = count;
        this.exchangeRate = exchangeRate;
        this.timestamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());
        volume = count * exchangeRate;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public double getVolume() {
        return volume;
    }

    /**
     * Compare to another order by date
     *
     * @param order - the order you want to compare to
     * @return - a positive number in case the current order date is more recent,
     * 0 if it is the same, or else a negative number
     */
    public int compareTo(final Order order) {
        return timestamp.compareTo(order.getTimestamp());
    }

    /**
     * Get basic information about the order
     * @return - the information as a string
     */
    public String getBasicInfo() {
        return "Date: " + timestamp + "\n" +
                "Number Of Stocks: " + count + "\n" +
                "Single Stock Price: " + exchangeRate + "\n" +
                "Total Order Price: " + volume;
    }
}
