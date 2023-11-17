package com.itmo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bars {
    private Map<String, List<Bar>> barData;  // Ключ - это символ актива, значение - список баров

    public Bars() {
        this.barData = new HashMap<>();
    }

    public void addBar(String symbol, Bar bar) {
        this.barData.computeIfAbsent(symbol, k -> new ArrayList<>()).add(bar);
    }

    public List<Double> getLatestBarValues(String symbol, String valueKey, int n) {
        // Возвращает последние 'n' значений для заданного символа
        // Например, для 'adj_close'
        // Метод требует реализации в зависимости от структуры Bar
        return null;
    }

    public Date getLatestBarDateTime(String symbol) {
        // Возвращает дату и время последнего бара для заданного символа
        // Метод требует реализации
        return null;
    }

    class Bar {
        private Date dateTime;
        private double open;
        private double high;
        private double low;
        private double close;
        private double adjClose;
        private long volume;
    }
}
