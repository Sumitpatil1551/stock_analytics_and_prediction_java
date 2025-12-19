package com.sumit.stock_analytics_backend.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndicatorService {

    // ================= RSI (14) =================
    public List<Double> calculateRsi(List<Double> closes) {

        final int period = 14;

        if (closes == null || closes.size() <= period) {
            return List.of();
        }

        List<Double> rsi = new ArrayList<>();
        double gain = 0, loss = 0;

        for (int i = 1; i <= period; i++) {
            double diff = closes.get(i) - closes.get(i - 1);
            if (diff >= 0) gain += diff;
            else loss -= diff;
        }

        gain /= period;
        loss /= period;

        double rs = loss == 0 ? 100 : gain / loss;
        rsi.add(100 - (100 / (1 + rs)));

        for (int i = period + 1; i < closes.size(); i++) {
            double diff = closes.get(i) - closes.get(i - 1);
            double g = diff > 0 ? diff : 0;
            double l = diff < 0 ? -diff : 0;

            gain = (gain * (period - 1) + g) / period;
            loss = (loss * (period - 1) + l) / period;

            rs = loss == 0 ? 100 : gain / loss;
            rsi.add(100 - (100 / (1 + rs)));
        }

        return rsi;
    }

    // ================= MACD (12,26,9) =================
    public List<Double> calculateMacd(List<Double> closes) {
        List<Double> ema12 = calculateEma(closes, 12);
        List<Double> ema26 = calculateEma(closes, 26);

        int size = Math.min(ema12.size(), ema26.size());
        if (size == 0) return List.of();

        List<Double> macd = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            macd.add(ema12.get(i) - ema26.get(i));
        }
        return macd;
    }

    public List<Double> calculateMacdSignal(List<Double> macd) {
        return calculateEma(macd, 9);
    }

    // ================= SMA 20 =================
    public List<Double> calculateSma20(List<Double> closes) {
        final int period = 20;
        if (closes.size() < period) return List.of();

        List<Double> sma = new ArrayList<>();
        for (int i = period - 1; i < closes.size(); i++) {
            double sum = 0;
            for (int j = i - period + 1; j <= i; j++) sum += closes.get(j);
            sma.add(sum / period);
        }
        return sma;
    }

    // ================= STD 20 =================
    public List<Double> calculateStd20(List<Double> closes) {
        final int period = 20;
        if (closes.size() < period) return List.of();

        List<Double> std = new ArrayList<>();
        for (int i = period - 1; i < closes.size(); i++) {
            double mean = 0;
            for (int j = i - period + 1; j <= i; j++) mean += closes.get(j);
            mean /= period;

            double var = 0;
            for (int j = i - period + 1; j <= i; j++)
                var += Math.pow(closes.get(j) - mean, 2);

            std.add(Math.sqrt(var / period));
        }
        return std;
    }

    // ================= OBV =================
    public List<Double> calculateObv(List<Double> closes, List<Double> volumes) {
        if (closes.size() < 2) return List.of();

        List<Double> obv = new ArrayList<>();
        double current = volumes.get(0);
        obv.add(current);

        for (int i = 1; i < closes.size(); i++) {
            if (closes.get(i) > closes.get(i - 1)) current += volumes.get(i);
            else if (closes.get(i) < closes.get(i - 1)) current -= volumes.get(i);
            obv.add(current);
        }
        return obv;
    }

    // ================= EMA helper =================
    private List<Double> calculateEma(List<Double> values, int period) {
        if (values.size() < period) return List.of();

        List<Double> ema = new ArrayList<>();
        double multiplier = 2.0 / (period + 1);

        double sum = 0;
        for (int i = 0; i < period; i++) sum += values.get(i);
        double prev = sum / period;
        ema.add(prev);

        for (int i = period; i < values.size(); i++) {
            double curr = (values.get(i) - prev) * multiplier + prev;
            ema.add(curr);
            prev = curr;
        }
        return ema;
    }
}
