package com.rohi.spring_mongo.e_commerce.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;


import java.io.Serializable;


// Ratings embedded document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesMetrics implements Serializable{
    private Integer totalSold;
    private Integer soldLastMonth;
    private Double revenue;
    private Integer viewCount;
}