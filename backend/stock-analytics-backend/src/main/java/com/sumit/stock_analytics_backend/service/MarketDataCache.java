package com.sumit.stock_analytics_backend.service;

import com.sumit.stock_analytics_backend.model.StockCandle;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MarketDataCache {

    private static final long TTL_SECONDS = 300; // 5 minutes

    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public List<StockCandle> get(String symbol) {
        CacheEntry entry = cache.get(symbol);
        if (entry == null) return null;
        if (Instant.now().getEpochSecond() - entry.timestamp > TTL_SECONDS) {
            cache.remove(symbol);
            return null;
        }
        return entry.data;
    }

    public void put(String symbol, List<StockCandle> data) {
        cache.put(symbol, new CacheEntry(data));
    }

    private static class CacheEntry {
        final List<StockCandle> data;
        final long timestamp = Instant.now().getEpochSecond();

        CacheEntry(List<StockCandle> data) {
            this.data = data;
        }
    }
}
