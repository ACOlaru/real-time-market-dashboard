package com.alexandra.dashboard.service;

import com.alexandra.dashboard.model.Trade;

public interface TradeSubscriber {
    void onTrade(Trade trade);
}
