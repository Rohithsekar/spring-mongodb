package com.rohi.spring_mongo.e_commerce.repository;

import com.rohi.spring_mongo.e_commerce.dto.request.ProductSearchRequest;
import com.rohi.spring_mongo.e_commerce.dto.view.ProductDetailsView;

import java.util.List;

//idiomatic name used in the Spring Data ecosystem for a class that contains custom, non-standard repository methods.
public interface ProductRepositoryCustom {
    List<ProductDetailsView> produceMatchingProductRecords(ProductSearchRequest searchRequest);
    long produceMatchingProductCount(ProductSearchRequest searchRequest);
}