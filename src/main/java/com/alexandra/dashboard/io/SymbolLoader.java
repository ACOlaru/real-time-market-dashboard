package com.alexandra.dashboard.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SymbolLoader {

    public static List<String> loadSymbols(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return List.of("AAPL", "TSLA", "AMZN", "GOOG", "MSFT");
        }
    }
}
