package com.alexandra.dashboard.ui;

import com.alexandra.dashboard.config.ConfigManager;
import com.alexandra.dashboard.service.MetricsCollector;
import com.alexandra.dashboard.service.MetricsCsvExporter;
import com.alexandra.dashboard.service.SymbolStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Dashboard implements Runnable {
    private final MetricsCollector metricsCollector = MetricsCollector.getInstance();
    private MetricsCsvExporter exporter;
    private final ConfigManager config = ConfigManager.getInstance();
    private final boolean useColors;
    private final int refreshInterval;
    private final int sparklineMaxHistory;

    private String RED = "";
    private String GREEN = "";
    private final String RESET = "\033[0m";
    private String YELLOW = "";
    private final int MAX_WIDTH = 20;

    public Dashboard() {
        useColors = config.getBoolean("dashboard.colors.enabled", true);
        refreshInterval = config.getInt("dashboard.refresh.interval", 1000);
        sparklineMaxHistory = config.getInt("sparkline.max.history", 20);

        boolean csvEnabled = config.getBoolean("export.csv.enabled", true);
        if (csvEnabled) {
            String csvPath = config.getString("export.csv.path", "report/metrics.csv");
            exporter = new MetricsCsvExporter(csvPath, metricsCollector);
        }

        if (useColors) {
            RED = "\033[0;31m";
            GREEN = "\033[0;32m";
            YELLOW = "\033[0;33m";
        }
    }

    @Override
    public void run() {
        while (true) {
            printDashboard();
            if (exporter != null) {
                exporter.exportMetrics();
            }

            try {
                Thread.sleep(refreshInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void printDashboard() {
        clearConsole();
        System.out.println("=== REAL-TIME MARKET METRICS DASHBOARD ===");
        System.out.println("Timestamp: " + LocalDateTime.now());
        System.out.println("-------------------------------------------");
        System.out.println("Trades per second: " + metricsCollector.getTradesPerSecond());
        printTopThreeTradedSymbols();

        printStatisticsPerSymbol();
        System.out.println("-------------------------------------------");
    }

    private void printStatisticsPerSymbol() {
        System.out.println("\nPer-Symbol Statistics:");
        System.out.printf("%-8s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-22s%n",
                "Symbol", "Trades", "AvgQty", "AvgPrice", "TotValue", "Min", "Max", "Buy/Sell", "Change%", "PriceTrend");

        for (Map.Entry<String, SymbolStats> entry : metricsCollector.getSymbolStats().entrySet()) {
            SymbolStats stats = entry.getValue();

            double change = stats.getPriceChangePercent();
            double buySellRatio = stats.getBuySellRatio();

            String changeColor = (useColors && change < 0) ? RED : GREEN;
            String ratioColor = (useColors && buySellRatio < 1) ? RED : GREEN;

            List<Double> priceHistory = stats.getPriceHistory();
            String sparkline = generateSparkline(priceHistory, sparklineMaxHistory);

            if (useColors && !priceHistory.isEmpty()) {
                double first = priceHistory.get(0);
                double last = priceHistory.get(priceHistory.size() - 1);
                String sparkColor = last >= first ? GREEN : RED;
                sparkline = sparkColor + sparkline + RESET;
            }

            System.out.printf("%-8s %-10d %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %s%-10.2f%s %s%-10.2f%s | %s%n",
                    entry.getKey(),
                    stats.getTradeCount(),
                    stats.getAverageQuantity(),
                    stats.getAveragePrice(),
                    stats.getTotalValue(),
                    stats.getMinPrice(),
                    stats.getMaxPrice(),
                    ratioColor, buySellRatio, RESET,
                    changeColor, change, RESET,
                    sparkline
            );
        }
    }

    private void printTopThreeTradedSymbols() {
        System.out.println(YELLOW + "Top 3 traded symbols:" + "\033[0m");
        metricsCollector.getTopTradedSymbols().forEach(entry ->
                System.out.printf("  %s (%d trades)%n", entry.getKey(), entry.getValue().getTradeCount())
        );
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private String generateSparkline(List<Double> history, int maxWidth) {
        if (history.isEmpty()) return "";
        double max = history.stream().max(Double::compareTo).orElse(1.0);
        double min = history.stream().min(Double::compareTo).orElse(0.0);
        char[] blocks = {'▁','▂','▃','▄','▅','▆','▇','█'};
        StringBuilder sb = new StringBuilder();
        for (double v : history) {
            int index = (int) ((v - min) / (max - min + 0.0001) * (blocks.length - 1));
            sb.append(blocks[Math.max(0, Math.min(index, blocks.length - 1))]);
        }
        return sb.toString();
    }

}
