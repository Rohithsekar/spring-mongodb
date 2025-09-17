package com.rohi.spring_mongo.e_commerce.utility;

import com.rohi.spring_mongo.e_commerce.dto.request.ProductSearchRequest;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

//This is a subtle but effective way of communicating to other developers that this class is
// intended to be managed by the framework. Making the constructor protected is a common idiom
// to prevent accidental manual instantiation. It allows Spring, which often operates through a proxy or
// subclassing mechanism, to access the constructor, while still preventing other classes in different packages
// from creating a new instance.

@Component
public class ProductAggregationUtility {

    protected ProductAggregationUtility() {
        // No-arg constructor required by Spring to manage as Singleton bean in its application context and for
        // dependency injection
    }

    public List<AggregationOperation> build(ProductSearchRequest searchRequest) {

        List<AggregationOperation> operations = new ArrayList<>();

        // 1. Build a single, comprehensive MatchOperation
        MatchOperation matchOperation = buildMatchOperation(searchRequest);

        operations.add(matchOperation);

        // 2. Add base aggregation operations (lookups, unwinds, projects)
        List<AggregationOperation> aggregationOperations = buildBaseAggregationPipeline();
        operations.addAll(aggregationOperations);

        return operations;
    }

    public Criteria buildMatchCriteria(ProductSearchRequest productSearchRequest) {
        String query = productSearchRequest.getQuery();
        String categoryId = productSearchRequest.getCategoryId();
        String brandId = productSearchRequest.getBrandId();
        Double minPrice = productSearchRequest.getMinPrice();
        Double maxPrice = productSearchRequest.getMaxPrice();
        Double minRating = productSearchRequest.getMinRating();
        List<String> tags = productSearchRequest.getTags();

        Criteria criteria = Criteria.where("isActive").is(true);

        if (Objects.nonNull(query)) {
            criteria.and("name").regex(Pattern.compile(query, Pattern.CASE_INSENSITIVE));
        }
        if (Objects.nonNull(categoryId)) {
            criteria.and("categoryId").is(categoryId);
        }
        if (Objects.nonNull(brandId)) {
            criteria.and("brandId").is(brandId);
        }
        if (Objects.nonNull(minPrice)) {
            criteria.and("pricing.currentPrice").gte(minPrice);
        }
        if (Objects.nonNull(maxPrice)) {
            criteria.and("pricing.currentPrice").lte(maxPrice);
        }

        if (Objects.nonNull(minRating)) {
            criteria.and("ratings.averageRating").gte(minRating);
        }
        if (Objects.nonNull(tags) && !tags.isEmpty()) {
            criteria.and("tags").in(tags);
        }

        return criteria;
    }

    private MatchOperation buildMatchOperation(ProductSearchRequest productSearchRequest) {
        Criteria criteria = buildMatchCriteria(productSearchRequest);
        return Aggregation.match(criteria);
    }

    private List<AggregationOperation> buildBaseAggregationPipeline() {
        List<AggregationOperation> operations = new ArrayList<>();

        // Using fluent API for readability and type-safety
        LookupOperation brandLookup = Aggregation.lookup("brands", "brandId", "_id", "brand");
        UnwindOperation brandUnwind = Aggregation.unwind("brand", true);

        operations.add(brandLookup);
        operations.add(brandUnwind);

        LookupOperation categoryLookup = Aggregation.lookup("categories", "categoryId", "_id", "category");
        UnwindOperation categoryUnwind = Aggregation.unwind("category", true);

        operations.add(categoryLookup);
        operations.add(categoryUnwind);

        // The project stage can remain a Document for this level of complexity
        AggregationOperation projectionOperation = (context) -> new Document("$project", new Document()
                .append("id", new Document("$toString", "$_id"))
                .append("name", 1)
                .append("slug", 1)
                .append("shortDescription", 1)
                .append("sku", 1)
                .append("pricingOnly", new Document("currentPrice", "$pricing.currentPrice")
                        .append("discountPercentage", "$pricing.discountPercentage"))
                .append("inventory", 1)
                .append("images", 1)
                .append("productAttributes", "$attributes")
                .append("ratings", 1)
                .append("salesMetrics", 1)
                .append("brand", new Document("id", new Document("$toString", "$brand._id"))
                        .append("name", "$brand.name")
                        .append("slug", "$brand.slug"))
                .append("category", new Document("id", new Document("$toString", "$category._id"))
                        .append("name", "$category.name")
                        .append("slug", "$category.slug")
                        .append("parentCategory", new Document("$toString", "$category.parentCategory")))
        );

        operations.add(projectionOperation);

        return operations;
    }


}
