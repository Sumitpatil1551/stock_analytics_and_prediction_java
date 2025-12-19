package com.sumit.stock_analytics_backend.controller;

import com.sumit.stock_analytics_backend.dto.CorrelationRequestDTO;
import com.sumit.stock_analytics_backend.dto.CorrelationResponseDTO;
import com.sumit.stock_analytics_backend.model.StockCandle;
import com.sumit.stock_analytics_backend.service.CorrelationService;
import com.sumit.stock_analytics_backend.client.MarketDataClient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/correlation")
public class CorrelationController {

    private final CorrelationService correlationService;
    private final MarketDataClient marketDataClient;

    public CorrelationController(
            CorrelationService correlationService,
            MarketDataClient marketDataClient
    ) {
        this.correlationService = correlationService;
        this.marketDataClient = marketDataClient;
    }

    @PostMapping
    public CorrelationResponseDTO correlate(
            @Valid @RequestBody CorrelationRequestDTO request
    ) {

        LocalDate to = LocalDate.now();
        LocalDate from = to.minusYears(1);

        Map<String, List<Double>> priceSeries = new HashMap<>();

        for (String symbol : request.getSymbols()) {
            List<StockCandle> candles =
                    marketDataClient.fetchDailyData(symbol, from, to);

            priceSeries.put(
                    symbol,
                    candles.stream()
                            .map(StockCandle::getClose)
                            .collect(Collectors.toList())
            );
        }

        return new CorrelationResponseDTO(
                correlationService
                        .calculateCorrelation(priceSeries)
                        .getMatrix()
        );
    }
}
