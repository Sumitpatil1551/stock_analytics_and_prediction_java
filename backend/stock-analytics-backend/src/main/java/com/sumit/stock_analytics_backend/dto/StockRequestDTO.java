package com.sumit.stock_analytics_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockRequestDTO {

    @NotBlank(message = "Stock symbol is required")
    private String symbol;

    @Positive(message = "Years must be positive")
    private int years = 1; // default: last 1 year
}
