package com.sumit.stock_analytics_backend.controller;

import com.sumit.stock_analytics_backend.dto.PredictionResponseDTO;
import com.sumit.stock_analytics_backend.service.PredictionService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping("/{symbol}")
    public PredictionResponseDTO predict(
            @NotBlank @PathVariable String symbol
    ) {
        // This will call Python ML service later
        return (PredictionResponseDTO) predictionService.predict(symbol);
    }
}
