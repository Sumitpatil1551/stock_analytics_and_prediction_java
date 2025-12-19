package com.sumit.stock_analytics_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CorrelationResponseDTO {

    private Map<String, Map<String, Double>> matrix;
}
