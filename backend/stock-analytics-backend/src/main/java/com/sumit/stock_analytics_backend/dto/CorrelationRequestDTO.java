package com.sumit.stock_analytics_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CorrelationRequestDTO {

    @NotEmpty(message = "At least two symbols are required")
    private List<String> symbols;
}
