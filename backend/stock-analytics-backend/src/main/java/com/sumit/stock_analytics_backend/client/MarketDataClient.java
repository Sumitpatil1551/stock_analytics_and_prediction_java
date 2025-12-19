package com.sumit.stock_analytics_backend.client;

import com.sumit.stock_analytics_backend.model.StockCandle;

import java.time.LocalDate;
import java.util.List;

public interface MarketDataClient {

    List<StockCandle> fetchDailyData(
            String symbol,
            LocalDate from,
            LocalDate to
    );
}
