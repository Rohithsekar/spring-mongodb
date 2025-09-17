package com.rohi.spring_mongo.e_commerce.repository;

import com.rohi.spring_mongo.e_commerce.dto.response.TopSellingProduct;
import com.rohi.spring_mongo.e_commerce.dto.view.ProductDetailsView;
import com.rohi.spring_mongo.e_commerce.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//for standard and simpler operations
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<ProductDetailsView> findAllBy(Pageable pageable);

    @Aggregation(pipeline = {
            "{ '$sort' : { 'salesMetrics.soldLastMonth' : -1 } }",
            "{ '$limit' : ?0 }",
            "{ '$project' : { 'name' : 1, 'currentPrice' : '$pricing.currentPrice', 'soldLastMonth' : '$salesMetrics.soldLastMonth', '_id' : 0 } }"
    })
    List<TopSellingProduct> findTopSellingProducts(Integer limit);

}
