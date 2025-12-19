package com.sumit.stock_analytics_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorResult {

    private String name;       // RSI, MACD, Bollinger Bands
    private GraphData primary; // Main line (e.g. RSI, MACD)
    private GraphData signal;  // Optional (MACD signal, %D line)
}
