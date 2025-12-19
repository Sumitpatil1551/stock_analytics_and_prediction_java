package com.sumit.stock_analytics_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponseDTO {

    private String symbol;

    // Dates for future prediction
    private List<String> forecastDates;

    // Predicted prices (next N days)
    private List<Double> predictedPrices;

    // Model evaluation metrics
    private Double trainRmse;
    private Double validationRmse;

    // Human-readable explanation
    private String modelSummary;
}
