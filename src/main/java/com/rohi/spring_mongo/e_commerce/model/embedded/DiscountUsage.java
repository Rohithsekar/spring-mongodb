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
public class DiscountUsage implements Serializable{
    private Integer totalUsed;
    private Double totalSavings;
    private Integer uniqueCustomers;
}