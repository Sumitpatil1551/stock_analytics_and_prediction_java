package com.sumit.stock_analytics_backend.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.stock_analytics_backend.exception.ApiException;
import com.sumit.stock_analytics_backend.model.StockCandle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Primary
@Component
public class AlphaVantageClient implements MarketDataClient {

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    @Value("${alpha.vantage.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<StockCandle> fetchDailyData(
            String symbol,
            LocalDate from,
            LocalDate to
    ) {

        String url = String.format(
                "%s?function=TIME_SERIES_DAILY&symbol=%s&outputsize=compact&apikey=%s",
                baseUrl,
                symbol,
                apiKey
        );

        try {
            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isBlank()) {
                throw new ApiException("Empty response from Alpha Vantage");
            }

            JsonNode root = objectMapper.readTree(response);

            if (root.has("Note")) {
                throw new ApiException("Alpha Vantage rate limit exceeded");
            }

            if (root.has("Error Message")) {
                throw new ApiException("Invalid symbol or API error");
            }

            JsonNode series = root.get("Time Series (Daily)");
            if (series == null || series.isEmpty()) {
                throw new ApiException("No daily market data available");
            }

            List<StockCandle> candles = new ArrayList<>();
            Iterator<String> dates = series.fieldNames();

            while (dates.hasNext()) {
                String dateStr = dates.next();
                LocalDate date = LocalDate.parse(dateStr);

                if (date.isBefore(from) || date.isAfter(to)) continue;

                JsonNode node = series.get(dateStr);

                candles.add(new StockCandle(
                        date,
                        node.get("1. open").asDouble(),
                        node.get("2. high").asDouble(),
                        node.get("3. low").asDouble(),
                        node.get("4. close").asDouble(),
                        node.get("5. volume").asDouble()
                ));
            }

            candles.sort((a, b) -> a.getDate().compareTo(b.getDate()));
            return candles;

        } catch (Exception e) {
            throw new ApiException("Failed to fetch daily data: " + e.getMessage());
        }
    }
}
