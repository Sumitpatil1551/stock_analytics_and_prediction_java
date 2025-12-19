package com.sumit.stock_analytics_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendSummaryDTO {

    private String trend;        // Bullish / Bearish / Sideways
    private double changePct;
    private String description;
}
