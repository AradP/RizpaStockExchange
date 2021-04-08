package models;

import javafx.beans.property.MapProperty;
import javafx.collections.ObservableMap;

import java.util.HashMap;

public class User {
    private String name;
    private HashMap<Stock, Integer> holdings;

    public User(String name, HashMap<Stock, Integer> holdings) {
        this.name = name;
        this.holdings = holdings;
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

    public HashMap<Stock, Integer> holdingsProperty() {
        return holdings;
    }

    public void setHoldings(HashMap<Stock, Integer> holdings) {
        this.holdings = holdings;
    }
}
