package com.rohi.spring_mongo.e_commerce.dto.response;

import com.rohi.spring_mongo.e_commerce.dto.view.ProductDetailsView;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long count;
    private List<ProductDetailsView> productDetailsViews;
//    private String id;
//    private String name;
//    private String slug;
//    private String description;
//    private String shortDescription;
//    private String sku;
//    private BrandSummary brand;
//    private CategorySummary category;
//    private Double currentPrice;
//    private Double originalPrice;
//    private Integer discountPercentage;
//    private Integer stock;
//    private Boolean inStock;
//    private Double averageRating;
//    private Integer totalReviews;
//    private List<String> images;
//    private List<String> tags;
//    private Boolean isFeatured;
}
