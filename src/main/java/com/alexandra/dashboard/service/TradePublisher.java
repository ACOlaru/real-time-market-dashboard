package com.alexandra.dashboard.service;

import com.alexandra.dashboard.model.Trade;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradePublisher {
    private final List<TradeSubscriber> subscribers = new CopyOnWriteArrayList<>();

    public void subscribe(TradeSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(TradeSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void publishTrade(Trade trade) {
        for (TradeSubscriber subscriber : subscribers) {
            subscriber.onTrade(trade);
        }
    }
}
