package com.rohi.spring_mongo.e_commerce.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {
    private String query;
    private String categoryId;
    private String brandId;
    private Double minPrice;
    private Double maxPrice;
    private Double minRating;
    private List<String> tags;
    private String sortBy; // price, rating, popularity, name
    private String sortDirection; // asc, desc
    private Integer page = 0;
    private Integer size = 20;
}