package com.sumit.stock_analytics_backend.client;

import com.sumit.stock_analytics_backend.model.StockCandle;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Primary
@Component
public class MockMarketDataClient implements MarketDataClient {

    @Override
    public List<StockCandle> fetchDailyData(
            String symbol,
            LocalDate from,
            LocalDate to
    ) {

        List<StockCandle> candles = new ArrayList<>();
        Random random = new Random();

        double price = 150.0;

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {

            // skip weekends
            if (date.getDayOfWeek().getValue() >= 6) continue;

            double open = price + random.nextDouble() * 2 - 1;
            double close = open + random.nextDouble() * 2 - 1;
            double high = Math.max(open, close) + random.nextDouble();
            double low = Math.min(open, close) - random.nextDouble();
            double volume = 1_000_000 + random.nextInt(500_000);

            candles.add(new StockCandle(
                    date,
                    open,
                    high,
                    low,
                    close,
                    volume
            ));

            price = close;
        }

        return candles;
    }
}
