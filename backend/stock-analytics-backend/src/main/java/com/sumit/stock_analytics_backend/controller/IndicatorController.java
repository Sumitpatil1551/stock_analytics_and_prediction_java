package com.sumit.stock_analytics_backend.controller;

import com.sumit.stock_analytics_backend.client.MarketDataClient;
import com.sumit.stock_analytics_backend.dto.GraphDataDTO;
import com.sumit.stock_analytics_backend.dto.IndicatorResponseDTO;
import com.sumit.stock_analytics_backend.dto.StockRequestDTO;
import com.sumit.stock_analytics_backend.model.StockCandle;
import com.sumit.stock_analytics_backend.service.IndicatorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/indicators")
public class IndicatorController {

    private final MarketDataClient marketDataClient;
    private final IndicatorService indicatorService;

    public IndicatorController(
            MarketDataClient marketDataClient,
            IndicatorService indicatorService
    ) {
        this.marketDataClient = marketDataClient;
        this.indicatorService = indicatorService;
    }

    // ---------------- RSI ----------------

    @PostMapping("/rsi")
    public IndicatorResponseDTO rsi(
            @Valid @RequestBody StockRequestDTO request
    ) {

        IndicatorContext ctx = loadContext(request);

        List<Double> rsiValues =
                indicatorService.calculateRsi(ctx.closes);

        if (rsiValues.isEmpty()) {
            return new IndicatorResponseDTO("RSI", null, null);
        }

        List<String> labels =
                trimLabels(ctx.labels, rsiValues.size());

        return new IndicatorResponseDTO(
                "RSI",
                new GraphDataDTO(labels, rsiValues),
                null
        );
    }

    // ---------------- MACD ----------------

    @PostMapping("/macd")
    public IndicatorResponseDTO macd(
            @Valid @RequestBody StockRequestDTO request
    ) {

        IndicatorContext ctx = loadContext(request);

        List<Double> macdValues =
                indicatorService.calculateMacd(ctx.closes);

        if (macdValues.isEmpty()) {
            return new IndicatorResponseDTO("MACD", null, null);
        }

        List<Double> signal =
                indicatorService.calculateMacdSignal(macdValues);

        List<String> labels =
                trimLabels(ctx.labels, macdValues.size());

        return new IndicatorResponseDTO(
                "MACD",
                new GraphDataDTO(labels, macdValues),
                new GraphDataDTO(labels, signal)
        );
    }

    // ---------------- OBV ----------------

    @PostMapping("/obv")
    public IndicatorResponseDTO obv(
            @Valid @RequestBody StockRequestDTO request
    ) {

        IndicatorContext ctx = loadContext(request);

        List<Double> obvValues =
                indicatorService.calculateObv(
                        ctx.closes, ctx.volumes
                );

        if (obvValues.isEmpty()) {
            return new IndicatorResponseDTO("OBV", null, null);
        }

        List<String> labels =
                trimLabels(ctx.labels, obvValues.size());

        return new IndicatorResponseDTO(
                "OBV",
                new GraphDataDTO(labels, obvValues),
                null
        );
    }

    // ---------------- helpers ----------------

    private IndicatorContext loadContext(StockRequestDTO request) {

        LocalDate to = LocalDate.now();
        LocalDate from = to.minusYears(request.getYears());

        List<StockCandle> candles =
                marketDataClient.fetchDailyData(
                        request.getSymbol(), from, to
                );

        List<String> labels = candles.stream()
                .map(c -> c.getDate().toString())
                .collect(Collectors.toList());

        return new IndicatorContext(
                labels,
                candles.stream().map(StockCandle::getClose).toList(),
                candles.stream().map(StockCandle::getVolume).toList()
        );
    }

    private List<String> trimLabels(List<String> labels, int dataSize) {
        if (labels == null || labels.isEmpty() || dataSize <= 0) {
            return List.of();
        }
        int diff = labels.size() - dataSize;
        return diff > 0 ? labels.subList(diff, labels.size()) : labels;
    }

    private record IndicatorContext(
            List<String> labels,
            List<Double> closes,
            List<Double> volumes
    ) {}
}
