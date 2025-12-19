package com.sumit.stock_analytics_backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AlphaVantageConfig {

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    @Value("${alpha.vantage.base.url:https://www.alphavantage.co/query}")
    private String baseUrl;
}
