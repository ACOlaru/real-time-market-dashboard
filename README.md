# Real-Time Market Metrics Dashboard

A **Java console application** simulating **real-time trading across multiple exchanges**, featuring live metrics, colored output, and ASCII sparklines.  
This project demonstrates **concurrency, design patterns, and real-time data processing**, showcasing advanced Java skills in a clean, modular architecture.

---

## 🚀 Features

- **Concurrent exchange simulation**: NYSE, NASDAQ, LSE, and more generating trades in real time.
- **Real-time metrics per symbol**:
    - Trades per second
    - Average trade quantity
    - Top 3 traded symbols
    - Min/Max prices
    - Buy/Sell ratios
- **Dynamic console dashboard**:
    - Colored output highlighting trends
    - ASCII sparklines visualizing price trends
- **CSV export** (optional) for metrics logging
- **Fully configurable** via `config.properties` and `symbols.txt`

---

## 💡 Technical Highlights

- **Concurrency**: Each exchange runs in its own thread simulating live trades.
- **Design patterns**:
    - **Observer** → `MetricsCollector` subscribes to all trade events
    - **Singleton** → Single instance of `MetricsCollector` managing metrics
    - **Command** → Each trade is a discrete, consistent event
- **Clean code principles**:
    - Modular structure: `model`, `service`, `ui`, `config`
    - Thread-safe metric calculations
    - Easy to extend for new exchanges or metrics
- **Configurable runtime**:
    - Exchanges, symbols, dashboard refresh interval
    - Sparkline length and colored output
    - CSV export file path

---

## System architecture

````

├── src/
│   └── main/
│       ├── java/
│       │   └── com/alexandra/dashboard/
│       │       ├── config/         # ConfigManager and configuration classes
│       │       ├── model/          # Trade, ActionEnum, SymbolStats
│       │       ├── service/        # ExchangeSimulator, MetricsCollector, MetricsCsvExporter, TradePublisher
│       │       └── ui/             # Dashboard (console display and visualization)
│       └── resources/
│           ├── config.properties   # Application configuration
│           └── symbols.txt         # List of symbols to simulate
├── report/                         # Generated CSV metrics (ignored by Git)
├── .gitignore                       # Ignore build files, reports, IDE configs
└── README.md                        # Project documentation

````

-------------------------------------------


## ⚡ Getting Started

### 1️⃣ Clone the repository

```bash
git clone https://github.com/ACOlaru/real-time-market-dashboard.git
cd real-time-market-dashboard

````

2️⃣ Configure settings

* config.properties:
  * Enable/disable CSV export
  * Set dashboard refresh interval
  * Define sparkline history length
  * Enable colored output

* symbols.txt: 
  * List of symbols to simulate (one per line)

3️⃣ Build & run

-------------------------------------------

## 📊 Sample Output
````
=== REAL-TIME MARKET METRICS DASHBOARD ===
Timestamp: 2025-11-09T15:34:12
-------------------------------------------
Trades per second: 45
Top 3 traded symbols:
  AAPL (120 trades)
  TSLA (98 trades)
  AMZN (87 trades)

Per-Symbol Statistics:
Symbol   Trades     AvgQty     AvgPrice   TotValue   Min        Max        Buy/Sell   Change%    PriceTrend
AAPL     120        45.3       132.45     15900.2    120.0      145.0      1.25       2.5        ▁▂▃▄▅▆▇█
TSLA     98         38.2       735.12     28000.3    710.0      750.0      0.95      -1.3        ▇▆▅▄▃▂▁
...

````

-------------------------------------------
## 🛠️ Technologies Used
* Java 17 → Records, Streams, Concurrency utilities
* Maven → Build and dependency management
* ANSI escape codes → Colored console output
* Design patterns → Observer, Singleton, Command
* File I/O → CSV export

-------------------------------------------

## 📈 Possible Extensions
* Integrate real market APIs for live data instead of simulated trades.
* Build a web or GUI dashboard (JavaFX, Swing, or Spring Boot).
* Add alerts for unusual activity or top movers.