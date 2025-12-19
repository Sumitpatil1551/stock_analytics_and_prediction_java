package com.sumit.stock_analytics_backend.client;

import com.sumit.stock_analytics_backend.exception.ApiException;
import com.sumit.stock_analytics_backend.model.StockCandle;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@Component
public class YahooFinanceCsvClient implements MarketDataClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<StockCandle> fetchDailyData(
            String symbol,
            LocalDate from,
            LocalDate to
    ) {

        long period1 = from.atStartOfDay(ZoneId.of("UTC")).toEpochSecond();
        long period2 = to.plusDays(1)
                .atStartOfDay(ZoneId.of("UTC"))
                .toEpochSecond();

        String url = String.format(
                "https://query1.finance.yahoo.com/v7/finance/download/%s" +
                        "?period1=%d&period2=%d&interval=1d&events=history&includeAdjustedClose=true",
                symbol,
                period1,
                period2
        );

        try {
            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isBlank()) {
                throw new ApiException("Empty response from Yahoo Finance");
            }

            // ❗ Yahoo sometimes returns HTML instead of CSV
            if (!response.startsWith("Date,Open")) {
                throw new ApiException(
                        "Yahoo Finance returned invalid data (possibly blocked)"
                );
            }

            String[] lines = response.split("\n");

            if (lines.length <= 1) {
                throw new ApiException(
                        "No historical data available for symbol: " + symbol
                );
            }

            List<StockCandle> candles = new ArrayList<>();

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();

                if (line.isEmpty()) continue;

                String[] parts = line.split(",");

                // Date,Open,High,Low,Close,Adj Close,Volume
                if (parts.length < 7) continue;
                if (parts[1].equals("null") || parts[4].equals("null")) continue;

                candles.add(new StockCandle(
                        LocalDate.parse(parts[0]),
                        Double.parseDouble(parts[1]),
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4]),
                        Double.parseDouble(parts[6])
                ));
            }

            if (candles.isEmpty()) {
                throw new ApiException(
                        "No usable market data found for symbol: " + symbol
                );
            }

            return candles;

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(
                    "Failed to fetch Yahoo Finance data: " + e.getMessage()
            );
        }
    }
}
