package com.sumit.stock_analytics_backend.indicator;

import java.util.ArrayList;
import java.util.List;

public class RsiCalculator {

    private RsiCalculator() {}

    public static List<Double> calculate(List<Double> closes, int period) {
        List<Double> rsi = new ArrayList<>();

        double gain = 0;
        double loss = 0;

        // Initial average gain/loss
        for (int i = 1; i <= period; i++) {
            double diff = closes.get(i) - closes.get(i - 1);
            if (diff >= 0) gain += diff;
            else loss -= diff;
        }

        gain /= period;
        loss /= period;

        rsi.add(null); // first value undefined

        for (int i = 1; i < closes.size(); i++) {
            if (i < period) {
                rsi.add(null);
                continue;
            }

            double diff = closes.get(i) - closes.get(i - 1);
            double currentGain = diff > 0 ? diff : 0;
            double currentLoss = diff < 0 ? -diff : 0;

            gain = ((gain * (period - 1)) + currentGain) / period;
            loss = ((loss * (period - 1)) + currentLoss) / period;

            double rs = loss == 0 ? 0 : gain / loss;
            double rsiValue = 100 - (100 / (1 + rs));

            rsi.add(rsiValue);
        }

        return rsi;
    }
}
