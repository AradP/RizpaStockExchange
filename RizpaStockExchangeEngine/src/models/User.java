package models;

import enums.Role;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String name;
    private HashMap<Stock, Integer> holdings;
    private Role role;
    private int currentMoney;
    private String alertMsg;

    private ArrayList<TradeActivity> tradeActivities;

    public User(String name, Role role) {
        this.name = name;
        this.role = role;
        this.currentMoney = 0;
        this.tradeActivities = new ArrayList<TradeActivity>();
        this.holdings = new HashMap<Stock, Integer>();
        this.alertMsg = "";
    }

    public User(String name, HashMap<Stock, Integer> holdings) {
        this.name = name;
        this.holdings = holdings;
        this.currentMoney = 0;
        this.tradeActivities = new ArrayList<TradeActivity>();
        this.holdings = new HashMap<Stock, Integer>();
        this.alertMsg = "";
    }

    public User(User user) {
        this.name = user.getName();
        this.role = user.getRole();
        this.holdings = user.getHoldings();
        this.currentMoney = 0;
        this.tradeActivities = new ArrayList<TradeActivity>();
        this.alertMsg = "";
        if (this.holdings == null) {
            this.holdings = new HashMap<Stock, Integer>();

        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Stock, Integer> getHoldings() {
        return holdings;
    }

    public int getCurrentMoney() {
        return this.currentMoney;
    }

    public void setCurrentMoney(int currentMoney) {
        this.currentMoney = currentMoney;
    }

    public String getAlertMsg() {
        return alertMsg.toString();
    }

    public void setAlertMsg(String alertMsg) {
        this.alertMsg = alertMsg;
    }

    public ArrayList<TradeActivity> getTradeActivities() {
        return tradeActivities;
    }

    public HashMap<Stock, Integer> holdingsProperty() {
        return holdings;
    }

    public void setHoldings(HashMap<Stock, Integer> holdings) {
        this.holdings = holdings;
    }

    public void addStocks(Stock stock, int amountOfStocks) {
        int oldAmount = holdings.containsKey(stock) ? holdings.get(stock) : 0;
        holdings.put(stock, oldAmount + amountOfStocks);
    }

    public void reduceStocks(Stock stock, int amountOfStocks) {
        holdings.put(stock, holdings.get(stock) - amountOfStocks);
        if (holdings.get(stock) == 0) {
            holdings.remove(stock);
        }
    }

    public void addMoney(int moneyToAdd) {
        getTradeActivities().add(new TradeActivity(moneyToAdd, this.currentMoney));
        this.currentMoney += moneyToAdd;
    }

    @Override
    public String toString() {
        return "name='" + name;
    }
}
