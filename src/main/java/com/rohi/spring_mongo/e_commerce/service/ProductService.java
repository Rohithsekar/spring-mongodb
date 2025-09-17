package com.rohi.spring_mongo.e_commerce.service;

import com.mongodb.MongoException;
import com.rohi.spring_mongo.e_commerce.constants.EcommerceConstants;
import com.rohi.spring_mongo.e_commerce.dto.request.ProductSearchRequest;
import com.rohi.spring_mongo.e_commerce.dto.response.ProductResponse;
import com.rohi.spring_mongo.e_commerce.dto.response.TopSellingProduct;
import com.rohi.spring_mongo.e_commerce.dto.view.ProductDetailsView;
import com.rohi.spring_mongo.e_commerce.repository.ProductRepository;
import com.rohi.spring_mongo.e_commerce.repository.ProductRepositoryCustom;
import com.rohi.spring_mongo.e_commerce.utility.ProductAggregationUtility;
import com.rohi.spring_mongo.global.dto.misc.BadRequestException;
import com.rohi.spring_mongo.global.dto.misc.ServerException;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ProductRepositoryCustom productRepositoryCustom;
    private final MongoTemplate mongoTemplate;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository,
                          ProductRepositoryCustom productRepositoryCustom,
                          MongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.productRepositoryCustom = productRepositoryCustom;
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

            List<ProductDetailsView> records = productRepositoryCustom.produceMatchingProductRecords(searchRequest);
            long count = productRepositoryCustom.produceMatchingProductCount(searchRequest);

            return new ProductResponse(count, records);
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


    public List<TopSellingProduct> getTopSellingProducts(Integer limit, HttpServletRequest servletRequest) {
        try {
            return productRepository.findTopSellingProducts(limit);
        } catch (DataAccessException e) {
            log.error("{} exception caught in getTopSellingProducts method : {}", e.getClass().getSimpleName(), e.getMessage());
            throw new ServerException("An unexpected error occurred while fetching top selling products.", servletRequest.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
