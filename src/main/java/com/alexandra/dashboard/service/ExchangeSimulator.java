package com.alexandra.dashboard.service;

import com.alexandra.dashboard.model.ActionEnum;
import com.alexandra.dashboard.model.Trade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class ExchangeSimulator implements Runnable {
    private final String exchangeName;
    private final TradePublisher publisher;
    private final List<String> symbols;
    private final Random random = new Random();

    private static final AtomicInteger counter = new AtomicInteger(1); // shared safely
    private static final double MIN_PRICE = 50.0;
    private static final double MAX_PRICE = 150.0;

    public ExchangeSimulator(String exchangeName, TradePublisher publisher, List<String> symbols) {
        this.exchangeName = exchangeName;
        this.publisher = publisher;
        this.symbols = symbols;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(200 + random.nextInt(800));
                Trade trade = generateTrade();
                publisher.publishTrade(trade);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private Trade generateTrade() {
        String tradeId = "T" + counter.getAndIncrement();
        String symbol = symbols.get(random.nextInt(symbols.size()));
        double price = MIN_PRICE + (MAX_PRICE - MIN_PRICE) * random.nextDouble();
        int quantity = 1 + random.nextInt(100);  // returns 1 to 100
        ActionEnum action = random.nextBoolean() ? ActionEnum.BUY : ActionEnum.SELL;
        LocalDateTime timestamp = LocalDateTime.now();
        return new Trade(tradeId, symbol, price, quantity, action, timestamp);
    }
}
