package com.alexandra.dashboard;

import com.alexandra.dashboard.io.ExchangeLoader;
import com.alexandra.dashboard.io.SymbolLoader;
import com.alexandra.dashboard.service.ExchangeSimulator;
import com.alexandra.dashboard.service.MetricsCollector;
import com.alexandra.dashboard.service.TradePublisher;
import com.alexandra.dashboard.ui.Dashboard;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        TradePublisher publisher = new TradePublisher();
        MetricsCollector collector = MetricsCollector.getInstance();

        publisher.subscribe(collector);

        List<String> symbols = SymbolLoader.loadSymbols("src/main/resources/symbols.txt");
        List<String> exchanges = ExchangeLoader.loadExchanges();

        for (String exchangeName : exchanges) {
            ExchangeSimulator simulator = new ExchangeSimulator(exchangeName, publisher, symbols);
            new Thread(simulator).start();
        }

        Thread dashboardThread = new Thread(new Dashboard());
        dashboardThread.start();

        System.out.println("✅ Real-Time Market Metrics Dashboard started!");
    }
}
