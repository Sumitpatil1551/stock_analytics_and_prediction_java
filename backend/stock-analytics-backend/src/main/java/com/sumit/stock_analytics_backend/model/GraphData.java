package com.sumit.stock_analytics_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphData {

    private List<String> labels;   // Dates or categories
    private List<Double> values;   // Numeric values
}
