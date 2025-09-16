package com.rohi.spring_mongo.e_commerce.model.embedded;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;


// Ratings embedded document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem implements Serializable {
    private String productId;
    private String productName;
    private String sku;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private Double discountAmount;
    private Double finalPrice;
}