package com.sumit.stock_analytics_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendSummary {

    private String trend;        // Bullish / Bearish / Sideways
    private double changePct;    // % change over period
    private String description;  // Text explanation
}
