package com.rohi.spring_mongo.e_commerce.service;

import com.mongodb.MongoException;
import com.rohi.spring_mongo.e_commerce.constants.EcommerceConstants;
import com.rohi.spring_mongo.e_commerce.dto.request.ProductSearchRequest;
import com.rohi.spring_mongo.e_commerce.dto.response.ProductResponse;
import com.rohi.spring_mongo.e_commerce.dto.view.ProductDetailsView;
import com.rohi.spring_mongo.e_commerce.repository.ProductRepository;
import com.rohi.spring_mongo.global.dto.misc.BadRequestException;
import com.rohi.spring_mongo.global.dto.misc.ServerException;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository,
                          MongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     *
     * @param searchRequest object wrapping the product search request fields
     * @return ProductResponse object wrapping the product search response fields
     */
    public ProductResponse searchProducts(ProductSearchRequest searchRequest, HttpServletRequest servletRequest) {
        try {
            //allowed sort or query fields
            List<String> allowedFields = List.of("brandId", "categoryId", "discountPercentage", "averageRating", "soldLastMonth");
            validateRequest(searchRequest, allowedFields, servletRequest);

            return applyFilters(searchRequest);
        } catch (MongoException e) {
            log.error("{} exception caught in addAuthors method : {}", e.getClass().getSimpleName(), e.getMessage());
            throw new ServerException("An unexpected error occurred while saving author records.", servletRequest.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void validateRequest(ProductSearchRequest productSearchRequest, List<String> allowedFields, HttpServletRequest servletRequest) {
        String sortField = productSearchRequest.getSortBy();

        if (allowedFields.stream().noneMatch(a -> a.equalsIgnoreCase(sortField))) {
            String error = String.format("invalid sort field: %s", sortField);
            log.error(error);
            throw new BadRequestException(error, servletRequest.getRequestURI());
        }
    }

    private ProductResponse applyFilters(ProductSearchRequest productSearchRequest) {

        // 1. Build the dynamic criteria based on searchRequest
        Criteria filterCriteria = buildFilterCriteria(productSearchRequest);

        // 2. Build the full aggregation pipeline
        List<AggregationOperation> aggregations = new ArrayList<>();
        if (!filterCriteria.getCriteriaObject().isEmpty()) {
            aggregations.add(Aggregation.match(filterCriteria));
        }

        // Add base aggregation operations (lookups, projects)
        aggregations.addAll(buildBaseAggregationPipeline());

        // 3. Add sorting and paging

        String sortField = getSortField(productSearchRequest.getSortBy());

        Sort sort = productSearchRequest.getSortDirection().equals("asc") ?
                Sort.by(sortField).ascending().and(Sort.by(EcommerceConstants.NESTED_AVERAGE_RATING).descending()) :
                Sort.by(sortField).descending().and(Sort.by(EcommerceConstants.NESTED_AVERAGE_RATING).descending());

        PageRequest pageRequest = PageRequest.of(productSearchRequest.getPage(), productSearchRequest.getSize(), sort);
        aggregations.add(Aggregation.skip(pageRequest.getOffset()));
        aggregations.add(Aggregation.limit(pageRequest.getPageSize()));
        aggregations.add(Aggregation.sort(sort));

        // 4. Execute the aggregation
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        AggregationResults<ProductDetailsView> results = mongoTemplate.aggregate(
                aggregation, "products", ProductDetailsView.class
        );

        List<ProductDetailsView> products = results.getMappedResults();

        // 5. Get the total count for paging, using the same filter criteria
        long totalRecords = countMatchingRecordsWithFilters(aggregations);

        return new ProductResponse(totalRecords, products);
    }

    private String getSortField(String sortBy) {
        if (sortBy.equalsIgnoreCase(EcommerceConstants.AVERAGE_RATING)) {
            return EcommerceConstants.NESTED_AVERAGE_RATING;
        } else if (sortBy.equalsIgnoreCase(EcommerceConstants.DISCOUNT_PERCENTAGE)) {
           return EcommerceConstants.NESTED_DISCOUNT_PERCENTAGE;
        } else if (sortBy.equalsIgnoreCase(EcommerceConstants.SOLD_LAST_MONTH)) {
            return EcommerceConstants.NESTED_SOLD_LAST_MONTH;
        }else{
            return sortBy;
        }
    }

    private Criteria buildFilterCriteria(ProductSearchRequest productSearchRequest) {
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


    private long countMatchingRecordsWithFilters(List<AggregationOperation> aggregationOperations) {
        List<AggregationOperation> countPipeline = new ArrayList<>();

        List<MatchOperation> matchOperations = aggregationOperations.stream()
                .filter(operation -> operation instanceof MatchOperation)
                .map(operation -> (MatchOperation) operation)
                .toList();

        if (!aggregationOperations.isEmpty() && !matchOperations.isEmpty()) {
            countPipeline.addAll(matchOperations); // Apply filters by re-using the match stage
        }

        countPipeline.add(Aggregation.count().as("total"));

        // Execute the count aggregation
        Aggregation countAggregation = Aggregation.newAggregation(countPipeline);
        return Optional.ofNullable(mongoTemplate.aggregate(countAggregation, "products", Document.class).getUniqueMappedResult()
        ).map(doc -> doc.getInteger("total")).orElse(0);

    }

    private List<AggregationOperation> buildBaseAggregationPipeline() {
        List<AggregationOperation> operations = new ArrayList<>();

        // Using fluent API for readability and type-safety
        LookupOperation brandLookup= Aggregation.lookup("brands", "brandId", "_id", "brand");
        UnwindOperation brandUnwind = Aggregation.unwind("brand", true);

        operations.add(brandLookup);
        operations.add(brandUnwind);

        LookupOperation categoryLookup = Aggregation.lookup("categories", "categoryId", "_id", "category");
        UnwindOperation categoryUnwind = Aggregation.unwind("category", true);

        operations.add(categoryLookup);
        operations.add(categoryUnwind);

        // The project stage can remain a Document for this level of complexity
       AggregationOperation projectionOperation =  (context) -> new Document("$project", new Document()
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
