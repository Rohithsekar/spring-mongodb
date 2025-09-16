package com.rohi.spring_mongo.e_commerce.dto.response;

import com.rohi.spring_mongo.e_commerce.model.embedded.Inventory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAvailabilityResponse {
    private String productId;
    private String sku;
    private Inventory inventory;
    private String availabilityMessage;
}