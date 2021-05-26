package models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Transaction implements Serializable {
    private String symbol;
    private String timeStamp;
    private int amountOfStocks;
    private double price;
    private User seller;
    private User buyer;

    public Transaction(String symbol, int amountOfStocks, double price, User seller, User buyer) {
        this.symbol = symbol;
        this.amountOfStocks = amountOfStocks;
        this.price = price;
        this.timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());
        this.seller = seller;
        this.buyer = buyer;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getAmountOfStocks() {
        return amountOfStocks;
    }

    public void setAmountOfStocks(int amountOfStocks) {
        this.amountOfStocks = amountOfStocks;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return price * amountOfStocks;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
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
            Date currentTimestamp = sdformat.parse(getTimeStamp());
            Date otherTimestamp = sdformat.parse(timestamp);
            return currentTimestamp.compareTo(otherTimestamp);
        } catch (ParseException e) {
            // We shouldn't get here as we are the ones who create the timestamp
            return -2;
        }
    }

    @Override
    public String toString() {
        return "Date: " + getTimeStamp() + "\n" +
                "Number Of Stocks: " + getAmountOfStocks() + "\n" +
                "Single Stock Price: " + getPrice() + "\n" +
                "Total Transaction volume: " + getVolume() + "\n" +
                "Seller: " + getSeller().getName() + "\n" +
                "Buyer: " + getBuyer().getName() + "\n";
    }
}
