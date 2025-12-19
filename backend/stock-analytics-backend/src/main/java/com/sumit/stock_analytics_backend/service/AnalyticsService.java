package com.sumit.stock_analytics_backend.service;

import com.sumit.stock_analytics_backend.client.MarketDataClient;
import com.sumit.stock_analytics_backend.model.StockCandle;
import com.sumit.stock_analytics_backend.model.TrendSummary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnalyticsService {

    private final MarketDataClient marketDataClient;
    private final IndicatorService indicatorService;

    public AnalyticsService(
            MarketDataClient marketDataClient,
            IndicatorService indicatorService
    ) {
        this.marketDataClient = marketDataClient;
        this.indicatorService = indicatorService;
    }

    public AnalyticsResult analyzeStock(String symbol, double years) {

        LocalDate to = LocalDate.now();
        LocalDate from = to.minusYears((long) years);

        List<StockCandle> candles =
                marketDataClient.fetchDailyData(symbol, from, to);

        List<Double> closes = candles.stream().map(StockCandle::getClose).toList();
        List<Double> volumes = candles.stream().map(StockCandle::getVolume).toList();

        return new AnalyticsResult(
                candles,
                indicatorService.calculateRsi(closes),
                indicatorService.calculateMacd(closes),
                indicatorService.calculateMacdSignal(
                        indicatorService.calculateMacd(closes)
                ),
                indicatorService.calculateSma20(closes),
                indicatorService.calculateStd20(closes),
                indicatorService.calculateObv(closes, volumes),
                calculateTrend(closes)
        );
    }

    private TrendSummary calculateTrend(List<Double> closes) {
        if (closes.size() < 2)
            return new TrendSummary("UNKNOWN", 0, "Not enough data");

        double change =
                ((closes.get(closes.size() - 1) - closes.get(0)) / closes.get(0)) * 100;

        String trend =
                change > 5 ? "BULLISH" :
                        change < -5 ? "BEARISH" :
                                "SIDEWAYS";

        return new TrendSummary(
                trend,
                Math.round(change * 100.0) / 100.0,
                "Daily trend analysis"
        );
    }
}
