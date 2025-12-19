package com.sumit.stock_analytics_backend.controller;

import com.sumit.stock_analytics_backend.dto.*;
import com.sumit.stock_analytics_backend.model.StockCandle;
import com.sumit.stock_analytics_backend.service.AnalyticsResult;
import com.sumit.stock_analytics_backend.service.AnalyticsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping
    public AnalyticsResponseDTO analyze(
            @Valid @RequestBody StockRequestDTO request
    ) {

        AnalyticsResult result =
                analyticsService.analyzeStock(
                        request.getSymbol(),
                        request.getYears()
                );

        List<StockCandle> candles = result.getCandles();

        List<String> labels = candles.stream()
                .map(c -> c.getDate().toString())
                .collect(Collectors.toList());

        GraphDataDTO price = new GraphDataDTO(
                labels,
                candles.stream().map(StockCandle::getClose).toList()
        );

        GraphDataDTO volume = new GraphDataDTO(
                labels,
                candles.stream().map(StockCandle::getVolume).toList()
        );

        return new AnalyticsResponseDTO(
                request.getSymbol(),
                price,
                volume,
                buildSingle("RSI", labels, result.getRsi()),
                buildDual("MACD", labels, result.getMacd(), result.getMacdSignal()),
                buildBollinger(labels, result.getSma20(), result.getStd20()),
                buildSingle("OBV", labels, result.getObv()),
                new TrendSummaryDTO(
                        result.getTrendSummary().getTrend(),
                        result.getTrendSummary().getChangePct(),
                        result.getTrendSummary().getDescription()
                )
        );
    }

    // ================= SAFE BUILDERS =================

    private IndicatorResponseDTO buildSingle(
            String name,
            List<String> labels,
            List<Double> values
    ) {
        if (values == null || values.isEmpty()) {
            return new IndicatorResponseDTO(name, null, null);
        }

        List<String> trimmed = trim(labels, values.size());

        return new IndicatorResponseDTO(
                name,
                new GraphDataDTO(trimmed, values),
                null
        );
    }

    private IndicatorResponseDTO buildDual(
            String name,
            List<String> labels,
            List<Double> primary,
            List<Double> signal
    ) {
        if (primary == null || primary.isEmpty()) {
            return new IndicatorResponseDTO(name, null, null);
        }

        List<String> trimmed = trim(labels, primary.size());

        GraphDataDTO signalGraph =
                (signal == null || signal.isEmpty())
                        ? null
                        : new GraphDataDTO(trimmed, signal);

        return new IndicatorResponseDTO(
                name,
                new GraphDataDTO(trimmed, primary),
                signalGraph
        );
    }

    private IndicatorResponseDTO buildBollinger(
            List<String> labels,
            List<Double> sma,
            List<Double> std
    ) {
        if (sma == null || std == null || sma.isEmpty() || std.isEmpty()) {
            return new IndicatorResponseDTO("Bollinger", null, null);
        }

        List<String> trimmed = trim(labels, sma.size());

        return new IndicatorResponseDTO(
                "Bollinger",
                new GraphDataDTO(trimmed, sma),
                new GraphDataDTO(trimmed, std)
        );
    }

    private List<String> trim(List<String> labels, int dataSize) {
        if (labels == null || labels.isEmpty() || dataSize <= 0) {
            return List.of();
        }
        int start = Math.max(0, labels.size() - dataSize);
        return labels.subList(start, labels.size());
    }
}
