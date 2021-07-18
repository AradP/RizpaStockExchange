package models;

import enums.TradeActivityType;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TradeActivity implements Serializable {
    private TradeActivityType tradeActivityType;
    private String symbol;
    private String timeStamp;
    private int price;
    private int moneyLeftBefore;
    private int moneyLeftAfter;

    public TradeActivityType getTradeActivityType() {
        return tradeActivityType;
    }
    public String getTradeActivityTypeAsString(){ return  tradeActivityType == TradeActivityType.BUY_STOCK? "Buy": tradeActivityType == TradeActivityType.SELL_STOCK?"Sell":"Add money";}

    public String getSymbol() {
        return symbol;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getPrice() {
        return price;
    }

    public int getMoneyLeftBefore() {
        return moneyLeftBefore;
    }

    public int getMoneyLeftAfter() {
        return moneyLeftAfter;
    }

    public TradeActivity(TradeActivityType tradeActivityType, String symbol, int price, int moneyLeftBefore) {
        this.tradeActivityType = tradeActivityType;
        this.symbol = symbol;
        this.price = price;
        if(tradeActivityType == TradeActivityType.BUY_STOCK){
            this.price *= -1;
        }
        this.moneyLeftBefore = moneyLeftBefore;
        this.moneyLeftAfter = moneyLeftBefore + price;
        this.timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());

    }

    public TradeActivity(int price, int moneyLeftBefore) {
        this.tradeActivityType = TradeActivityType.ADD_MONEY;
        this.symbol = "";
        this.price = price;
        this.moneyLeftBefore = moneyLeftBefore;
        this.moneyLeftAfter = moneyLeftBefore + price;
        this.timeStamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());
    }

    public int compareByDate(final String timestamp) {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date currentTimestamp = sdformat.parse(this.timeStamp);
            Date otherTimestamp = sdformat.parse(timestamp);
            return currentTimestamp.compareTo(otherTimestamp);
        } catch (ParseException e) {
            // We shouldn't get here as we are the ones who create the timestamp
            return -2;
        }
    }
}