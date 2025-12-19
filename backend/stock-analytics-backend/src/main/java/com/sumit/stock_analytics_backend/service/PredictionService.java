package com.sumit.stock_analytics_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PredictionService {

    private final RestTemplate restTemplate = new RestTemplate();

    // External Python ML service (future)
    private static final String ML_SERVICE_URL =
            "http://localhost:5000/predict";

    public Map<String, Object> predict(String symbol) {

        Map<String, String> request =
                Map.of("symbol", symbol);

        return restTemplate.postForObject(
                ML_SERVICE_URL,
                request,
                Map.class
        );
    }
}
