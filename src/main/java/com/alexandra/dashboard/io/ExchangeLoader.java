package com.alexandra.dashboard.io;

import com.alexandra.dashboard.config.ConfigManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeLoader {

    public static List<String> loadExchanges() {
        ConfigManager config = ConfigManager.getInstance();
        String exchangesStr = config.getString("exchanges", "NYSE,NASDAQ,LSE");
        return Arrays.stream(exchangesStr.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
