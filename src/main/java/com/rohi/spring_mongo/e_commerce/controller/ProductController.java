package com.rohi.spring_mongo.e_commerce.controller;


import com.rohi.spring_mongo.author.dto.response.APIResponse;
import com.rohi.spring_mongo.e_commerce.dto.request.ProductSearchRequest;
import com.rohi.spring_mongo.e_commerce.dto.response.ProductResponse;
import com.rohi.spring_mongo.e_commerce.service.ProductService;
import com.rohi.spring_mongo.global.constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Search products with filters and pagination
     * GET /api/products/search?query=iphone&minPrice=500&maxPrice=1000&page=0&size=20
     */
    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String brandId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "averageRating") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            HttpServletRequest httpServletRequest) {

        ProductSearchRequest searchRequest = ProductSearchRequest.builder()
                .query(query)
                .categoryId(categoryId)
                .brandId(brandId)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .minRating(minRating)
                .tags(tags)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .page(page)
                .size(size)
                .build();

        ProductResponse response = productService.searchProducts(searchRequest, httpServletRequest);
        return ResponseEntity.ok(new APIResponse(Constants.SUCCESS, "products fetched successfully", response));
    }

//    /**
//     * Get product by ID with full details
//     * GET /api/products/{id}
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
//        ProductResponse product = productService.getProductById(id);
//        return ResponseEntity.ok(product);
//    }
//
//    /**
//     * Get featured products
//     * GET /api/products/featured
//     */
//    @GetMapping("/featured")
//    public ResponseEntity<List<ProductResponse>> getFeaturedProducts() {
//        List<ProductResponse> products = productService.getFeaturedProducts();
//        return ResponseEntity.ok(products);
//    }
//
//    /**
//     * Get products by category
//     * GET /api/products/category/{categoryId}
//     */
//    @GetMapping("/category/{categoryId}")
//    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
//            @PathVariable String categoryId,
//            @RequestParam(defaultValue = "0") Integer page,
//            @RequestParam(defaultValue = "20") Integer size) {
//
//        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, page, size);
//        return ResponseEntity.ok(products);
//    }
//
//    /**
//     * Get products by brand
//     * GET /api/products/brand/{brandId}
//     */
//    @GetMapping("/brand/{brandId}")
//    public ResponseEntity<Page<ProductResponse>> getProductsByBrand(
//            @PathVariable String brandId,
//            @RequestParam(defaultValue = "0") Integer page,
//            @RequestParam(defaultValue = "20") Integer size) {
//
//        Page<ProductResponse> products = productService.getProductsByBrand(brandId, page, size);
//        return ResponseEntity.ok(products);
//    }
//
//    /**
//     * Get top selling products
//     * GET /api/products/top-selling?limit=10
//     */
//    @GetMapping("/top-selling")
//    public ResponseEntity<List<ProductResponse>> getTopSellingProducts(
//            @RequestParam(defaultValue = "10") Integer limit) {
//
//        List<ProductResponse> products = productService.getTopSellingProducts(limit);
//        return ResponseEntity.ok(products);
//    }
//
//    /**
//     * Get product recommendations
//     * GET /api/products/{id}/recommendations
//     */
//    @GetMapping("/{id}/recommendations")
//    public ResponseEntity<List<ProductResponse>> getProductRecommendations(
//            @PathVariable String id,
//            @RequestParam(defaultValue = "5") Integer limit) {
//
//        List<ProductResponse> recommendations = productService.getProductRecommendations(id, limit);
//        return ResponseEntity.ok(recommendations);
//    }
//
//    /**
//     * Check product availability
//     * GET /api/products/{id}/availability
//     */
//    @GetMapping("/{id}/availability")
//    public ResponseEntity<ProductAvailabilityResponse> checkProductAvailability(@PathVariable String id) {
//        ProductAvailabilityResponse availability = productService.checkProductAvailability(id);
//        return ResponseEntity.ok(availability);
//    }
}