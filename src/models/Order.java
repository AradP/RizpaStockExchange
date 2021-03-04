package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private String _symbol;
    private String _timestamp;
    private int _count;
    private double _exchangeRate;
    private double _volume;

    public Order(String symbol, int count, double exchangeRate) {
        _symbol = symbol;
        _count = count;
        _exchangeRate = exchangeRate;
        _timestamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());
        _volume = _count * _exchangeRate;
    }
}
