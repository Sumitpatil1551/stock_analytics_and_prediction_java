package com.sumit.stock_analytics_backend.indicator;

import java.util.ArrayList;
import java.util.List;

public class MacdCalculator {

    private MacdCalculator() {}

    public static List<Double> ema(List<Double> data, int period) {
        List<Double> ema = new ArrayList<>();
        double multiplier = 2.0 / (period + 1);

        ema.add(data.get(0));

        for (int i = 1; i < data.size(); i++) {
            double value = (data.get(i) - ema.get(i - 1)) * multiplier + ema.get(i - 1);
            ema.add(value);
        }
        return ema;
    }

    public static List<Double> calculateMacd(List<Double> closes) {
        List<Double> ema12 = ema(closes, 12);
        List<Double> ema26 = ema(closes, 26);

        List<Double> macd = new ArrayList<>();
        for (int i = 0; i < closes.size(); i++) {
            macd.add(ema12.get(i) - ema26.get(i));
        }
        return macd;
    }

    public static List<Double> calculateSignal(List<Double> macd) {
        return ema(macd, 9);
    }
}
