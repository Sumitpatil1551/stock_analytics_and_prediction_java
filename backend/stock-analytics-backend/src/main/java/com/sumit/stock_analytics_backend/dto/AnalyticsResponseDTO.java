package com.sumit.stock_analytics_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponseDTO {

    private String symbol;

    private GraphDataDTO price;        // close prices
    private GraphDataDTO volume;

    private IndicatorResponseDTO rsi;
    private IndicatorResponseDTO macd;
    private IndicatorResponseDTO bollingerBands;
    private IndicatorResponseDTO stochastic;
    private IndicatorResponseDTO obv;

    private TrendSummaryDTO trendSummary;

    public AnalyticsResponseDTO(@NotBlank(message = "Stock symbol is required") String symbol, GraphDataDTO price, GraphDataDTO volume, IndicatorResponseDTO rsi, IndicatorResponseDTO macd, IndicatorResponseDTO bollinger, IndicatorResponseDTO obv, TrendSummaryDTO trendSummaryDTO) {

    }
}
