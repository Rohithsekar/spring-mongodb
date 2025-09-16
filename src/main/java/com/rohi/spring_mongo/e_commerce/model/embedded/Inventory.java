package com.rohi.spring_mongo.e_commerce.model.embedded;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// Inventory embedded document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory implements Serializable {
    private Integer stock;
    private Integer lowStockThreshold;
    private Boolean isInStock;
    private String warehouseLocation;
}