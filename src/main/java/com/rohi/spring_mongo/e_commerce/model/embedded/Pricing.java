package com.rohi.spring_mongo.e_commerce.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;


// Pricing embedded document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pricing implements Serializable {
    private Double basePrice;
    private Double currentPrice;
    private Integer discountPercentage;
    private Double costPrice;
    private String currency;
}