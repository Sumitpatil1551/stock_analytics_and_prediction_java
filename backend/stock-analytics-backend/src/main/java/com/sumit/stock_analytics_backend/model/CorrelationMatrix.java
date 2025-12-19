package com.sumit.stock_analytics_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrelationMatrix {

    /*
     * Example:
     * {
     *   "AAPL": { "MSFT": 0.82, "GOOG": 0.76 },
     *   "MSFT": { "AAPL": 0.82, "GOOG": 0.70 }
     * }
     */
    private Map<String, Map<String, Double>> matrix;
}
