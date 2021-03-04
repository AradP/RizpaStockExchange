package stocks;

import java.util.Date;

public class StockTransaction {
    private Date date;
    private int numOfStocks;
    private int singleStockPrice;
    private int totalStockTransactionPrice;

    public StockTransaction(Date date, int numOfStocks, int singleStockPrice, int totalStockTransactionPrice) {
        this.date = date;
        this.numOfStocks = numOfStocks;
        this.singleStockPrice = singleStockPrice;
        this.totalStockTransactionPrice = totalStockTransactionPrice;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public int getNumOfStocks() {
        return this.numOfStocks;
    }

    public void setNumOfStocks(final int numOfStocks) {
        if (numOfStocks > 0) {
            this.numOfStocks = numOfStocks;
        }
    }

    public int getSingleStockPrice() {
        return this.singleStockPrice;
    }

    public void setSingleStockPrice(final int singleStockPrice) {
        if (singleStockPrice > 0) {
            this.singleStockPrice = singleStockPrice;
        }
    }

    public int getTotalStockTransactionPrice() {
        return this.totalStockTransactionPrice;
    }

    public void setTotalStockTransactionPrice(final int totalStockTransactionPrice) {
        this.totalStockTransactionPrice = totalStockTransactionPrice;
    }

    /**
     * Compare to another stocks.StockTransaction by date
     * @param stockTransaction - the stockTransaction you want to compare to
     * @return - a positive number in case the current stockTransaction date is more recent,
     * 0 if it is the same, or else a negative number
     */
    public int compareTo(final StockTransaction stockTransaction) {
        return this.getDate().compareTo(stockTransaction.getDate());
    }

    // TODO: Change to the right date format
    public String getBasicInfo() {
        return "Date: " + date + "\n" +
                "Number Of Stocks: " + numOfStocks + "\n" +
                "Single Stock Price: " + singleStockPrice + "\n" +
                "Total Stock Transaction Price: " + totalStockTransactionPrice;
    }
}
