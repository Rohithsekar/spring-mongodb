package com.rohi.spring_mongo.e_commerce.service;

import com.mongodb.MongoException;
import com.rohi.spring_mongo.e_commerce.dto.request.ProductSearchRequest;
import com.rohi.spring_mongo.e_commerce.dto.response.ProductResponse;
import com.rohi.spring_mongo.e_commerce.dto.view.ProductDetailsView;
import com.rohi.spring_mongo.e_commerce.model.Product;
import com.rohi.spring_mongo.e_commerce.repository.ProductRepository;
import com.rohi.spring_mongo.global.dto.misc.BadRequestException;
import com.rohi.spring_mongo.global.dto.misc.ServerException;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        //    /**
        //     * Search products with filters and pagination
        //     * GET /api/products/search?query=iphone&minPrice=500&maxPrice=1000&page=0&size=20
        //     */
        //    @GetMapping("/search")
        //    public ResponseEntity<Page<ProductResponse>> searchProducts(
        //            @RequestParam(required = false) String query,
        //            @RequestParam(required = false) String categoryId,
        //            @RequestParam(required = false) String brandId,
        //            @RequestParam(required = false) Double minPrice,
        //            @RequestParam(required = false) Double maxPrice,
        //            @RequestParam(required = false) Double minRating,
        //            @RequestParam(required = false) List<String> tags,
        //            @RequestParam(defaultValue = "popularity") String sortBy,
        //            @RequestParam(defaultValue = "desc") String sortDirection,
        //            @RequestParam(defaultValue = "0") Integer page,
        //            @RequestParam(defaultValue = "20") Integer size) {

        try {
            //first construct the basic Product query without filters
            //allowed sort or query fields

            //pricing.discountPercentage -> discountPercentage
            //salesMetrics.soldLastMonth -> soldLastMonth
            //ratings.averageRating -> averageRating
            List<String> allowedFields = List.of("brandId", "categoryId", "discountPercentage", "averageRating", "soldLastMonth");
            validateRequest(searchRequest, allowedFields, servletRequest);

            return applyFilters(searchRequest);
            //basic query-> no


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

        String query = productSearchRequest.getQuery();
        String categoryId = productSearchRequest.getCategoryId();
        String brandId = productSearchRequest.getBrandId();
        Double minPrice = productSearchRequest.getMinPrice();
        Double maxPrice = productSearchRequest.getMaxPrice();
        Double minRating = productSearchRequest.getMinRating();
        List<String> tags = productSearchRequest.getTags();

        Sort sort = productSearchRequest.getSortDirection().equals("asc") ? Sort.by(productSearchRequest.getSortBy()).ascending().and(Sort.by("averageRating").descending()) :
                Sort.by(productSearchRequest.getSortBy()).descending().and(Sort.by("averageRating").descending());

        PageRequest pageRequest = PageRequest.of(productSearchRequest.getPage(), productSearchRequest.getSize(), sort);


        if (Objects.nonNull(query) && Objects.nonNull(categoryId) && Objects.nonNull(brandId) && Objects.nonNull(minPrice)
                && Objects.nonNull(maxPrice) && Objects.nonNull(minRating) && Objects.nonNull(tags) && !tags.isEmpty()) {

        } else if (Objects.nonNull(categoryId) && Objects.nonNull(brandId) && Objects.nonNull(minPrice)
                && Objects.nonNull(maxPrice) && Objects.nonNull(minRating) && Objects.nonNull(tags) && !tags.isEmpty()) {

        } else if (Objects.nonNull(brandId) && Objects.nonNull(minPrice)
                && Objects.nonNull(maxPrice) && Objects.nonNull(minRating) && Objects.nonNull(tags) && !tags.isEmpty()) {

        } else if (Objects.nonNull(minPrice) && Objects.nonNull(maxPrice) && Objects.nonNull(minRating)
                && Objects.nonNull(tags) && !tags.isEmpty()) {

        } else if (Objects.nonNull(maxPrice) && Objects.nonNull(minRating) && Objects.nonNull(tags) && !tags.isEmpty()) {

        } else if (Objects.nonNull(minRating) && Objects.nonNull(tags) && !tags.isEmpty()) {

        } else if (Objects.nonNull(tags) && !tags.isEmpty()) {

        } else {
            return getProductsWithNoFilters(pageRequest);

        }
        return null;
    }

    private ProductResponse getProductsWithNoFilters(Pageable pageable) {
//
//        // Test with one specific product
//        MatchOperation matchOne = Aggregation.match(
//                Criteria.where("_id").is(new ObjectId("64a4b2c3d4e5f6789012345a"))
//        );
//
//        LookupOperation brandLookup = LookupOperation.newLookup()
//                .from("brands")
//                .localField("brandId")
//                .foreignField("_id")
//                .as("brand");
//
//        LookupOperation categoryLookup = LookupOperation.newLookup()
//                .from("categories")
//                .localField("categoryId")
//                .foreignField("_id")
//                .as("category");
//
//        // DON'T unwind yet - let's see the arrays first
//        ProjectionOperation debugProject = Aggregation.project()
//                .and("_id").as("id")
//                .and("name").as("name")
//                .and("brandId").as("brandId")
//                .and("categoryId").as("categoryId")
//                .and("brand").as("brand")        // This should be an array
//                .and("category").as("category"); // This should be an array
//
//        Aggregation debugAggregation = Aggregation.newAggregation(
//                matchOne,
//                brandLookup,
//                categoryLookup,
//                debugProject
//        );
//
//        AggregationResults<Document> results = mongoTemplate.aggregate(
//                debugAggregation, "products", Document.class
//        );
//
//        for (Document doc : results.getMappedResults()) {
//            System.out.println("=== DEBUG RESULT ===");
//            System.out.println("Product ID: " + doc.get("id"));
//            System.out.println("Product Name: " + doc.get("name"));
//            System.out.println("Brand ID: " + doc.get("brandId"));
//            System.out.println("Category ID: " + doc.get("categoryId"));
//            System.out.println("Brand Array: " + doc.get("brand"));
//            System.out.println("Category Array: " + doc.get("category"));
//            System.out.println("Raw Document: " + doc.toJson());
//            System.out.println("==================");
//        }
//        return null;

        Sort sort = pageable.getSort();
        SortOperation sortOperation = sort.isSorted() ? Aggregation.sort(sort) : null;
////
//        // Stage 1: $lookup for Brand
//        LookupOperation brandLookup = LookupOperation.newLookup()
//                .from("brands")
//                .localField("brandId")
//                .foreignField("_id")
//                .as("brand");
//
//        // Stage 2: $unwind the brand array
//        UnwindOperation brandUnwind = Aggregation.unwind("brand", true);
//
//        // Stage 3: $lookup for Category
//        LookupOperation categoryLookup = LookupOperation.newLookup()
//                .from("categories")
//                .localField("categoryId")
//                .foreignField("_id")
//                .as("category");
//
//        // Stage 4: $unwind the category array
//        UnwindOperation categoryUnwind = Aggregation.unwind("category", true);
//
//        // Stage 5: Project the final fields
//        ProjectionOperation project = Aggregation.project()
//                .and("_id").as("id")
//                .and("name").as("name")
//                .and("slug").as("slug")
//                .and("shortDescription").as("shortDescription")
//                .and("sku").as("sku")
//                .and("pricing.currentPrice").as("pricingOnly.currentPrice")
//                .and("pricing.discountPercentage").as("pricingOnly.discountPercentage")
//                .and("inventory").as("inventory")
//                .and("images").as("images")
//                .and("attributes").as("productAttributes")
//                .and("ratings").as("ratings")
//                .and("salesMetrics").as("salesMetrics")
//                // After unwind, brand and category are objects, not arrays
//                .and("brand._id").as("brand.id")
//                .and("brand.name").as("brand.name")
//                .and("brand.slug").as("brand.slug")
//                .and("category._id").as("category.id")
//                .and("category.name").as("category.name")
//                .and("category.slug").as("category.slug")
//                .and("category.parentCategory").as("category.parentCategory");
//
//        SkipOperation skip = Aggregation.skip(pageable.getOffset());
//        LimitOperation limit = Aggregation.limit(pageable.getPageSize());
//
//        List<AggregationOperation> operations = new ArrayList<>();
//        operations.add(brandLookup);
//        operations.add(brandUnwind);
//        operations.add(categoryLookup);
//        operations.add(categoryUnwind);
//
//        if (sortOperation != null) {
//            operations.add(sortOperation);
//        }
//
//        operations.add(project);
//        operations.add(skip);
//        operations.add(limit);
//
//        Aggregation aggregation = Aggregation.newAggregation(operations);
//
//        AggregationResults<ProductDetailsView> results = mongoTemplate.aggregate(
//                aggregation, "products", ProductDetailsView.class
//        );
//
//        long totalRecords = mongoTemplate.count(new Query(), Product.class);
//
//        List<ProductDetailsView> productDetailsViews = PageableExecutionUtils.getPage(
//                results.getMappedResults(),
//                pageable,
//                () -> totalRecords
//        ).getContent();
//
//        return new ProductResponse(totalRecords, productDetailsViews);
//    }

        // Use custom aggregation operations to handle ObjectId conversion
        AggregationOperation brandLookup = context -> new Document("$lookup",
                new Document("from", "brands")
                        .append("localField", "brandId")
                        .append("foreignField", "_id")
                        .append("as", "brand")
        );

        AggregationOperation brandUnwind = context -> new Document("$unwind",
                new Document("path", "$brand")
                        .append("preserveNullAndEmptyArrays", true)
        );

        AggregationOperation categoryLookup = context -> new Document("$lookup",
                new Document("from", "categories")
                        .append("localField", "categoryId")
                        .append("foreignField", "_id")
                        .append("as", "category")
        );

        AggregationOperation categoryUnwind = context -> new Document("$unwind",
                new Document("path", "$category")
                        .append("preserveNullAndEmptyArrays", true)
        );

        AggregationOperation project = context -> new Document("$project",
                new Document("id", new Document("$toString", "$_id"))
                        .append("name", 1)
                        .append("slug", 1)
                        .append("shortDescription", 1)
                        .append("sku", 1)
                        .append("pricingOnly", new Document()
                                .append("currentPrice", "$pricing.currentPrice")
                                .append("discountPercentage", "$pricing.discountPercentage"))
                        .append("inventory", 1)
                        .append("images", 1)
                        .append("productAttributes", "$attributes")
                        .append("ratings", 1)
                        .append("salesMetrics", 1)
                        .append("brand", new Document()
                                .append("id", new Document("$toString", "$brand._id")) // Corrected: Project _id from the joined brand document
                                .append("name", "$brand.name")
                                .append("slug", "$brand.slug"))
                        .append("category", new Document()
                                .append("id", new Document("$toString", "$category._id")) // Corrected: Project _id from the joined category document
                                .append("name", "$category.name")
                                .append("slug", "$category.slug")
                                .append("parentCategory", new Document("$toString", "$category.parentCategory")))
        );

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(brandLookup);
        operations.add(brandUnwind);
        operations.add(categoryLookup);
        operations.add(categoryUnwind);

        if (sort.isSorted()) {
            operations.add(Aggregation.sort(sort));
        }

        operations.add(project);
        operations.add(Aggregation.skip(pageable.getOffset()));
        operations.add(Aggregation.limit(pageable.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(operations);

        AggregationResults<ProductDetailsView> results = mongoTemplate.aggregate(
                aggregation, "products", ProductDetailsView.class
        );

        long totalRecords = mongoTemplate.count(new Query(), Product.class);

        List<ProductDetailsView> productDetailsViews = PageableExecutionUtils.getPage(
                results.getMappedResults(),
                pageable,
                () -> totalRecords
        ).getContent();

        return new ProductResponse(totalRecords, productDetailsViews);
    }
}
