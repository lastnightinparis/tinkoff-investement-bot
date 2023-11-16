package com.itmo.model;


import com.itmo.dto.TradeSignal;

import java.util.LinkedList;
import java.util.Queue;

public class EventQueue {
    private Queue<TradeSignal> events;

    public EventQueue() {
        this.events = new LinkedList<>();
    }

    public void put(TradeSignal event) {
        events.add(event);
    }

    public TradeSignal get() {
        return events.poll(); // Возвращает и удаляет головной элемент или возвращает null, если очередь пуста
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}
