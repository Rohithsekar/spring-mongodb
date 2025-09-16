package com.rohi.spring_mongo.e_commerce.dto.view;

import com.rohi.spring_mongo.e_commerce.model.embedded.Inventory;
import com.rohi.spring_mongo.e_commerce.model.embedded.ProductAttributes;
import com.rohi.spring_mongo.e_commerce.model.embedded.Ratings;
import com.rohi.spring_mongo.e_commerce.model.embedded.SalesMetrics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsView {
    private String id;
    private String name;
    private String slug;
    private String shortDescription;
    private String sku;
    private BrandSummary brand;
    private CategorySummary category;
    private PricingOnly pricingOnly;
    private Inventory inventory;
    private List<String> images;
    private ProductAttributes productAttributes;
    private Ratings ratings;
    private SalesMetrics salesMetrics;

    // You also need to define the nested classes/interfaces within this class
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricingOnly {
        private Double currentPrice;
        private Double discountPercentage;
    }
}