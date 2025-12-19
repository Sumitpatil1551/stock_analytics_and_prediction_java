package com.sumit.stock_analytics_backend.indicator;

import java.util.ArrayList;
import java.util.List;

public class StochasticCalculator {

    private StochasticCalculator() {}

    public static List<Double> calculateK(
            List<Double> highs,
            List<Double> lows,
            List<Double> closes,
            int period
    ) {
        List<Double> kValues = new ArrayList<>();

        for (int i = 0; i < closes.size(); i++) {
            if (i < period) {
                kValues.add(null);
                continue;
            }

            double highestHigh = highs.subList(i - period, i)
                    .stream().mapToDouble(Double::doubleValue).max().orElse(0);

            double lowestLow = lows.subList(i - period, i)
                    .stream().mapToDouble(Double::doubleValue).min().orElse(0);

            double k = ((closes.get(i) - lowestLow) /
                    (highestHigh - lowestLow)) * 100;

            kValues.add(k);
        }
        return kValues;
    }

    public static List<Double> smooth(List<Double> values, int period) {
        List<Double> smoothed = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            if (i < period || values.get(i) == null) {
                smoothed.add(null);
                continue;
            }

            double sum = 0;
            int count = 0;
            for (int j = i - period; j < i; j++) {
                if (values.get(j) != null) {
                    sum += values.get(j);
                    count++;
                }
            }
            smoothed.add(count == 0 ? null : sum / count);
        }
        return smoothed;
    }
}
