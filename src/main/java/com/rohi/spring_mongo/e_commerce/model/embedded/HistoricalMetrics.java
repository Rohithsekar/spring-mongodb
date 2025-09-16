package com.rohi.spring_mongo.e_commerce.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricalMetrics implements Serializable{
    private Integer unitsSold;
    private Double revenue;
    private Double averageOrderValue;
    private Integer uniqueCustomers;
    private Integer returnsCount;
    private Double refundAmount;
}