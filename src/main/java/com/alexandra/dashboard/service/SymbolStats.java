package com.alexandra.dashboard.service;

import com.alexandra.dashboard.model.ActionEnum;
import com.alexandra.dashboard.model.Trade;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * ------------------------------
 * Metrics tracked per symbol:
 * ------------------------------
 * 1. tradeCount          → Number of trades received for this symbol.
 * 2. totalQuantity       → Sum of all trade quantities (total traded volume).
 * 3. averageQuantity     → Average trade quantity (totalQuantity / tradeCount).
 * 4. totalValue          → Sum of all trade values (price × quantity).
 * 5. averagePrice        → Average trade price (simple average across trades).
 * 6. minPrice / maxPrice → Lowest and highest observed trade prices.
 * 7. buyCount / sellCount→ Number of BUY and SELL trades (used for sentiment).
 * 8. buySellRatio        → Ratio of BUY to SELL trades (market sentiment indicator).
 * 9. firstPrice / lastPrice → Used to compute price change percentage.
 * 10. priceChangePercent → Percentage change from first to latest price.
 */

public class SymbolStats {
    private final int MAX_HISTORY = 20;
    private int tradeCount = 0;
    private double totalQuantity = 0.0;
    private double totalValue = 0.0;
    private double totalPrice = 0.0;
    private double minPrice = Double.MAX_VALUE;
    private double maxPrice = Double.MIN_VALUE;
    private int buyCount = 0;
    private int sellCount = 0;
    private double firstPrice = -1;
    private double lastPrice = -1;
    private final Deque<Double> priceHistory = new ArrayDeque<>();


    synchronized void update(Trade trade) {
        tradeCount++;
        totalQuantity += trade.quantity();
        totalValue += trade.price() * trade.quantity();
        totalPrice += trade.price();

        if (minPrice == 0 || trade.price() < minPrice) {
            minPrice = trade.price();
        }

        if (maxPrice == 0 || trade.price() > maxPrice) {
            maxPrice = trade.price();
        }

        if (trade.action() == ActionEnum.BUY) buyCount++;
        else sellCount++;

        if (firstPrice == -1) firstPrice = trade.price();
        lastPrice = trade.price();
        addPrice(trade.price());
    }

    synchronized public int getTradeCount() {
        return tradeCount;
    }

    synchronized public double getAverageQuantity() {
        return tradeCount == 0 ? 0 : totalQuantity / tradeCount;
    }

    synchronized public double getAveragePrice() {
        return tradeCount == 0 ? 0 : totalPrice / tradeCount;
    }

    synchronized public double getTotalValue() {
        return totalValue;
    }

    synchronized public double getMinPrice() {
        return minPrice == Double.MAX_VALUE ? 0 : minPrice;
    }

    synchronized public double getMaxPrice() {
        return maxPrice == Double.MIN_VALUE ? 0 : maxPrice;
    }

    synchronized public double getBuySellRatio() {
        return sellCount == 0 ? buyCount : (double) buyCount / sellCount;
    }

    synchronized public double getPriceChangePercent() {
        if (firstPrice <= 0 || lastPrice <= 0) {
            return 0;
        }

        return ((lastPrice - firstPrice) / firstPrice) * 100;
    }

    synchronized void addPrice(double price) {
        if (priceHistory.size() >= MAX_HISTORY) {
            priceHistory.removeFirst();
        }
        priceHistory.addLast(price);
    }

    public synchronized List<Double> getPriceHistory() {
        return new ArrayList<>(priceHistory);
    }
}
