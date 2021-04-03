package models;

import enums.OrderType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Stock implements Serializable {
    private String symbol;
    private String companyName;
    private int price;
    private ArrayList<Transaction> completedTransactions;
    private ArrayList<Order> pendingSellOrders;

    private ArrayList<Order> pendingBuyOrders;

    public Stock(String symbol, String companyName, int price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.completedTransactions = new ArrayList<>();
        this.pendingSellOrders = new ArrayList<>();
        this.pendingBuyOrders = new ArrayList<>();
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

    public void setPrice(final int price) {
        if (price > 0) {
            this.price = price;
        }
    }

    public ArrayList<Order> getPendingSellOrders() {
        return pendingSellOrders;
    }

    public ArrayList<Order> getPendingBuyOrders() {
        return pendingBuyOrders;
    }

    public ArrayList<Transaction> getCompletedTransactions() {
        return completedTransactions;
    }

    private int getTheHighestPriceInPendingSellOrders() {
        if (getPendingSellOrders().isEmpty()) {
            return (int) getPrice();
        } else {
            return (int) getPendingSellOrders().get(getPendingSellOrders().size() - 1).getRequestedExchangeRate();
        }
    }

    private int getTheLowestPriceInPendingBuyOrders() {
        if (getPendingBuyOrders().isEmpty()) {
            return (int) getPrice();
        } else {
            return (int) getPendingBuyOrders().get(getPendingBuyOrders().size() - 1).getRequestedExchangeRate();
        }
    }

    public boolean CreateSellOrder(int amountOfStocks, double requestedExchangeRate, OrderType orderType) {
        boolean didSuccess = false;
        Order newSellOrder = new Order(getSymbol(), amountOfStocks, requestedExchangeRate, orderType);
        didSuccess = pendingSellOrders.add(newSellOrder);
        if (didSuccess) {
            pendingSellOrders.sort(Comparator.comparingDouble(Order::getRequestedExchangeRate));
        }

        return didSuccess;
    }

    public boolean CreateSellOrderMKT(int amountOfStocks) {
        boolean didSuccess = false;
        Order newSellOrder = new Order(getSymbol(), amountOfStocks, getTheLowestPriceInPendingBuyOrders(), OrderType.MKT);
        didSuccess = pendingSellOrders.add(newSellOrder);
        if (didSuccess) {
            pendingSellOrders.sort(Comparator.comparingDouble(Order::getRequestedExchangeRate));
        }

        return didSuccess;
    }


    public boolean CreateBuyOrderMKT(int amountOfStocks) {
        return CreateBuyOrder(amountOfStocks, getTheHighestPriceInPendingSellOrders(), OrderType.MKT);
    }

    public boolean CreateBuyOrder(int amountOfStocks, double requestedExchangeRate, OrderType orderType) {
        boolean didSuccess = false;
        Order newBuyOrder = new Order(getSymbol(), amountOfStocks, requestedExchangeRate, orderType);
        didSuccess = pendingBuyOrders.add(newBuyOrder);
        if (didSuccess) {
            pendingBuyOrders.sort(Comparator.comparingDouble(Order::getRequestedExchangeRate).reversed());
        }
        return didSuccess;
    }

    public boolean IsItPossibleToMakeATransaction() {
        boolean isItPossibleToMakeATransaction = false;
        if ((pendingSellOrders.size() * pendingBuyOrders.size()) != 0) {
            isItPossibleToMakeATransaction = pendingSellOrders.get(0).getRequestedExchangeRate() <= pendingBuyOrders.get(0).getRequestedExchangeRate();
        }

        return isItPossibleToMakeATransaction;
    }

    public boolean IsItPossibleToMakeATransactionFOK(boolean isSell, int amountOfStocks, double requestedExchangeRate) {
        ArrayList<Order> oppositeOrders = isSell ? pendingBuyOrders : pendingSellOrders;
        int currentCell = 0;
        while (amountOfStocks > 0 && oppositeOrders.size() > currentCell &&
                isSell ? requestedExchangeRate <= oppositeOrders.get(currentCell).getRequestedExchangeRate() :
                requestedExchangeRate >= oppositeOrders.get(currentCell).getRequestedExchangeRate()) {
            amountOfStocks -= oppositeOrders.get(currentCell++).getCount();
        }

        return amountOfStocks == 0;
    }

    /**
     * @param isSell
     * @param amountOfStocks
     * @param requestedExchangeRate
     * @return 0 - it is possible to make a transaction
     * x>0 - it is possible to make a transaction but only half
     * -1 - not possible to make a transaction.
     */
    public int IsItPossibleToMakeATransactionIOC(boolean isSell, int amountOfStocks, double requestedExchangeRate) {
        ArrayList<Order> oppositeOrders = isSell ? pendingBuyOrders : pendingSellOrders;
        int currentCell = 0;
        int tempAmountOfStocks = amountOfStocks;
        while (tempAmountOfStocks > 0 && oppositeOrders.size() > currentCell &&
                isSell ? requestedExchangeRate <= oppositeOrders.get(currentCell).getRequestedExchangeRate() :
                requestedExchangeRate >= oppositeOrders.get(currentCell).getRequestedExchangeRate()) {
            tempAmountOfStocks -= oppositeOrders.get(currentCell++).getCount();
        }

        return tempAmountOfStocks == amountOfStocks ? -1 : tempAmountOfStocks <= 0 ? 0 : tempAmountOfStocks;
    }


    public ArrayList<Transaction> makeATransaction(boolean isSell) {
        ArrayList<Transaction> newTransactions = new ArrayList<>();
        boolean isBuy = !isSell;

        while (IsItPossibleToMakeATransaction()) {
            Order sellOrder = pendingSellOrders.get(0);
            Order buyOrder = pendingBuyOrders.get(0);

            int maxAmountOfStocksThatPossible = Integer.min(sellOrder.getCount(), buyOrder.getCount());
            double stockPrice = sellOrder.getRequestedExchangeRate() * (isBuy ? 1 : 0) + buyOrder.getRequestedExchangeRate() * (isSell ? 1 : 0);
            setPrice((int) stockPrice);

            Transaction newTransaction = new Transaction(getSymbol(), maxAmountOfStocksThatPossible, stockPrice);
            newTransactions.add(newTransaction);
            sellOrder.reduceCount(maxAmountOfStocksThatPossible);
            buyOrder.reduceCount(maxAmountOfStocksThatPossible);

            if (sellOrder.getCount() == 0) {
                pendingSellOrders.remove(0);
            }
            if (buyOrder.getCount() == 0) {
                pendingBuyOrders.remove(0);
            }
        }

        completedTransactions.addAll(newTransactions);


        return newTransactions;
    }

    /**
     * Returns the transactions sorted by date
     *
     * @return the sorted transactions
     */
    public ArrayList<Transaction> getTransactionsSortedByDate() {
        ArrayList<Transaction> sortedTransactions = new ArrayList<>(getCompletedTransactions());
        Collections.reverse(sortedTransactions);
        return sortedTransactions;
    }

    /**
     * Get basic information about the stock
     *
     * @return - the information as a string
     */
    @Override
    public String toString() {
        double ordersPeriod = 0;
        final ArrayList<Transaction> sortedTransactions = getCompletedTransactions();

        // Calculate orders period
        for (Transaction transaction : sortedTransactions) {
                ordersPeriod += transaction.getVolume();
        }

        return "Symbol: " + symbol + "\n" +
                "Company Name: " + companyName + "\n" +
                "Single Stock Price: " + price + "\n" +
                "Total transactions number: " + getCompletedTransactions().size() + "\n" +
                "Total transactions volume: " + ordersPeriod + "\n";
    }
}
