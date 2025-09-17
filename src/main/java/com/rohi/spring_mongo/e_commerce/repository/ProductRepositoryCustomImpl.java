package com.rohi.spring_mongo.e_commerce.repository;

import com.rohi.spring_mongo.e_commerce.dto.request.ProductSearchRequest;
import com.rohi.spring_mongo.e_commerce.dto.view.ProductDetailsView;
import com.rohi.spring_mongo.e_commerce.utility.ProductAggregationUtility;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    private final ProductAggregationUtility aggregationUtility;

    public ProductRepositoryCustomImpl(MongoTemplate mongoTemplate, ProductAggregationUtility aggregationUtility) {
        this.mongoTemplate = mongoTemplate;
        this.aggregationUtility = aggregationUtility;
    }

    @Override
    public long produceMatchingProductCount(ProductSearchRequest searchRequest) {
        Criteria filterCriteria = aggregationUtility.buildMatchCriteria(searchRequest);
        Query query = new Query(filterCriteria);
        return mongoTemplate.count(query, "products");
    }

    @Override
    public List<ProductDetailsView> produceMatchingProductRecords(ProductSearchRequest searchRequest) {
        List<AggregationOperation> pipeline = aggregationUtility.build(searchRequest);

        Aggregation aggregation = Aggregation.newAggregation(pipeline);
        AggregationResults<ProductDetailsView> results = mongoTemplate.aggregate(
                aggregation, "products", ProductDetailsView.class
        );

        return results.getMappedResults();
    }
}