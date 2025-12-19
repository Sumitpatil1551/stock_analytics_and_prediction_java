package com.sumit.stock_analytics_backend.service;

import com.sumit.stock_analytics_backend.model.CorrelationMatrix;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CorrelationService {

    public CorrelationMatrix calculateCorrelation(
            Map<String, List<Double>> priceSeries
    ) {
        Map<String, Map<String, Double>> matrix = new HashMap<>();

        for (String s1 : priceSeries.keySet()) {
            Map<String, Double> row = new HashMap<>();
            for (String s2 : priceSeries.keySet()) {
                row.put(s2, pearson(
                        priceSeries.get(s1),
                        priceSeries.get(s2)
                ));
            }
            matrix.put(s1, row);
        }

        return new CorrelationMatrix(matrix);
    }

    private double pearson(List<Double> x, List<Double> y) {
        int n = Math.min(x.size(), y.size());

        double meanX = x.stream().mapToDouble(Double::doubleValue)
                .average().orElse(0);
        double meanY = y.stream().mapToDouble(Double::doubleValue)
                .average().orElse(0);

        double num = 0, denX = 0, denY = 0;

        for (int i = 0; i < n; i++) {
            double dx = x.get(i) - meanX;
            double dy = y.get(i) - meanY;
            num += dx * dy;
            denX += dx * dx;
            denY += dy * dy;
        }

        return num / Math.sqrt(denX * denY);
    }
}
