package models;

import enums.OrderType;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Order implements Serializable {
    private String symbol;
    private String timestamp;
    private int count;
    private double requestedExchangeRate;
    private OrderType orderType;

    public Order(String symbol, int count, double requestedExchangeRate, OrderType orderType) {
        this.symbol = symbol;
        this.count = count;
        this.requestedExchangeRate = requestedExchangeRate;
        this.timestamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());
        this.orderType = orderType;
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
    public void reduceCount(int countToReduce){
        this.count -= countToReduce;
    }

    public double getRequestedExchangeRate() {
        return requestedExchangeRate;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public double getVolume() {
        return count * requestedExchangeRate;
    }


    /**
     * Compare to another order by date
     *
     * @param timestamp - the timestamp you want to compare to
     * @return - a positive number in case the current order date is more recent,
     * 0 if it is the same, or else a negative number
     */
    public int compareByDate(final String timestamp) {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date currentTimestamp = sdformat.parse(this.timestamp);
            Date otherTimestamp = sdformat.parse(timestamp);
            return currentTimestamp.compareTo(otherTimestamp);
        } catch (ParseException e) {
            // We shouldn't get here as we are the ones who create the timestamp
            return -2;
        }
    }

    @Override
    public String toString() {
        return "Date: " + timestamp + "\n" +
                "Number Of Stocks: " + count + "\n" +
                "Single Stock Price: " + requestedExchangeRate + "\n" +
                "Total Order Price: " + getVolume() + "\n";
    }
}
