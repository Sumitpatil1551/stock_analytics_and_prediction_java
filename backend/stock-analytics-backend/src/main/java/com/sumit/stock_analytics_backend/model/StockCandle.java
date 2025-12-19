package com.sumit.stock_analytics_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockCandle {

    private LocalDate date;

    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
}
