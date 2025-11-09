package com.alexandra.dashboard.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class MetricsCsvExporter {

    private final String filePath;
    private final MetricsCollector metricsCollector;

    public MetricsCsvExporter(String filePath, MetricsCollector metricsCollector) {
        this.filePath = filePath;
        this.metricsCollector = metricsCollector;

        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeHeader();
    }

    private void writeHeader() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("Timestamp,Symbol,Trades,AvgQty,AvgPrice,TotValue,MinPrice,MaxPrice,Buy/Sell,ChangePercent");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportMetrics() {
        Map<String, SymbolStats> statsMap = metricsCollector.getSymbolStats();
        LocalDateTime now = LocalDateTime.now();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Map.Entry<String, SymbolStats> entry : statsMap.entrySet()) {
                String line = getStatsLine(entry, now);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getStatsLine(Map.Entry<String, SymbolStats> entry, LocalDateTime now) {
        SymbolStats stats = entry.getValue();
        return String.format("%s,%s,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f",
                now,
                entry.getKey(),
                stats.getTradeCount(),
                stats.getAverageQuantity(),
                stats.getAveragePrice(),
                stats.getTotalValue(),
                stats.getMinPrice(),
                stats.getMaxPrice(),
                stats.getBuySellRatio(),
                stats.getPriceChangePercent()
        );
    }
}
