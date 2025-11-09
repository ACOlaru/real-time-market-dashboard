package com.alexandra.dashboard.service;


import com.alexandra.dashboard.model.Trade;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MetricsCollector implements TradeSubscriber {
    private static MetricsCollector instance;

    private final Map<String, SymbolStats> symbolStatsMap = new ConcurrentHashMap<>();
    private final AtomicInteger totalTradesThisSecond = new AtomicInteger(0);

    private MetricsCollector() {}

    public static synchronized MetricsCollector getInstance() {
        if (instance == null) {
            instance = new MetricsCollector();
        }
        return instance;
    }

    @Override
    public void onTrade(Trade trade) {
        totalTradesThisSecond.incrementAndGet();

        symbolStatsMap.compute(trade.symbol(), (symbol, stats) -> {
            if (stats == null) stats = new SymbolStats();
            stats.update(trade);
            return stats;
        });
    }

    public int getTradesPerSecond() {
        return totalTradesThisSecond.getAndSet(0);
    }

    public Map<String, SymbolStats> getSymbolStats() {
        return symbolStatsMap;
    }

    public List<Map.Entry<String, SymbolStats>> getTopTradedSymbols() {
        return symbolStatsMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().getTradeCount(), a.getValue().getTradeCount()))
                .limit(3)
                .collect(Collectors.toList());
    }
}
