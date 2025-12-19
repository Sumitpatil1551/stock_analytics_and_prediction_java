package com.sumit.stock_analytics_backend.client;

import com.sumit.stock_analytics_backend.exception.ApiException;
import com.sumit.stock_analytics_backend.model.StockCandle;
import com.sumit.stock_analytics_backend.service.MarketDataCache;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class FallbackMarketDataClient implements MarketDataClient {

    private final YahooFinanceCsvClient yahoo;
    private final AlphaVantageClient alpha;
    private final MarketDataCache cache;

    public FallbackMarketDataClient(
            YahooFinanceCsvClient yahoo,
            AlphaVantageClient alpha,
            MarketDataCache cache
    ) {
        this.yahoo = yahoo;
        this.alpha = alpha;
        this.cache = cache;
    }

    @Override
    public List<StockCandle> fetchDailyData(
            String symbol,
            LocalDate from,
            LocalDate to
    ) {

        // 1️⃣ Cache first
        List<StockCandle> cached = cache.get(symbol);
        if (cached != null) {
            return cached;
        }

        // 2️⃣ Try Yahoo
        try {
            List<StockCandle> data = yahoo.fetchDailyData(symbol, from, to);
            cache.put(symbol, data);
            return data;
        } catch (Exception ignored) {}

        // 3️⃣ Fallback to Alpha Vantage
        try {
            List<StockCandle> data = alpha.fetchDailyData(symbol, from, to);
            cache.put(symbol, data);
            return data;
        } catch (Exception e) {
            throw new ApiException(
                    "All market data providers are currently unavailable"
            );
        }
    }
}
