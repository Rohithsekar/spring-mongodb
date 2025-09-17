package com.rohi.spring_mongo.e_commerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopSellingProduct {
    private String name;
    private Double currentPrice;
    private Integer soldLastMonth;
}
