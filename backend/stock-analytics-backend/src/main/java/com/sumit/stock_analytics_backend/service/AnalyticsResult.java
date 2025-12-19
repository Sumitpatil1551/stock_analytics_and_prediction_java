package com.sumit.stock_analytics_backend.service;

import com.sumit.stock_analytics_backend.model.StockCandle;
import com.sumit.stock_analytics_backend.model.TrendSummary;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnalyticsResult {

    private List<StockCandle> candles;

    private List<Double> rsi;

    private List<Double> macd;
    private List<Double> macdSignal;

    private List<Double> sma20;
    private List<Double> std20;

    private List<Double> stochasticK;
    private List<Double> stochasticD;

    private List<Double> obv;

    private TrendSummary trendSummary;

    public AnalyticsResult(List<StockCandle> candles, List<Double> rsi, List<Double> macd, List<Double> macdSignal, List<Double> sma, List<Double> std, List<Double> obv, TrendSummary trendSummary) {
    }
}
