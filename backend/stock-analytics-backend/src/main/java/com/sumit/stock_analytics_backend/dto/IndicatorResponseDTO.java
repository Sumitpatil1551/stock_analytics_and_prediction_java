package com.sumit.stock_analytics_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorResponseDTO {

    private String name;

    private GraphDataDTO primary; // main line
    private GraphDataDTO signal;  // optional (MACD signal, %D)
}
