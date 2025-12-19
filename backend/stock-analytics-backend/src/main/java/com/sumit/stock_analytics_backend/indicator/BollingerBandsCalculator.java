package com.sumit.stock_analytics_backend.indicator;

import java.util.ArrayList;
import java.util.List;

public class BollingerBandsCalculator {

    private BollingerBandsCalculator() {}

    public static List<Double> sma(List<Double> data, int period) {
        List<Double> sma = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (i < period) {
                sma.add(null);
                continue;
            }

            double sum = 0;
            for (int j = i - period; j < i; j++) {
                sum += data.get(j);
            }
            sma.add(sum / period);
        }
        return sma;
    }

    public static List<Double> std(List<Double> data, int period) {
        List<Double> std = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (i < period) {
                std.add(null);
                continue;
            }

            double mean = 0;
            for (int j = i - period; j < i; j++) {
                mean += data.get(j);
            }
            mean /= period;

            double variance = 0;
            for (int j = i - period; j < i; j++) {
                variance += Math.pow(data.get(j) - mean, 2);
            }
            variance /= period;

            std.add(Math.sqrt(variance));
        }
        return std;
    }
}
