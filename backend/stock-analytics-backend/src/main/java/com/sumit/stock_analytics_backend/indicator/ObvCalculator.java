package com.sumit.stock_analytics_backend.indicator;

import java.util.ArrayList;
import java.util.List;

public class ObvCalculator {

    private ObvCalculator() {}

    public static List<Double> calculate(List<Double> closes, List<Double> volumes) {
        List<Double> obv = new ArrayList<>();
        obv.add(0.0);

        for (int i = 1; i < closes.size(); i++) {
            if (closes.get(i) > closes.get(i - 1)) {
                obv.add(obv.get(i - 1) + volumes.get(i));
            } else if (closes.get(i) < closes.get(i - 1)) {
                obv.add(obv.get(i - 1) - volumes.get(i));
            } else {
                obv.add(obv.get(i - 1));
            }
        }
        return obv;
    }
}
