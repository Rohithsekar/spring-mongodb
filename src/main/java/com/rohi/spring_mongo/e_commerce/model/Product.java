package com.rohi.spring_mongo.e_commerce.model;

import com.rohi.spring_mongo.e_commerce.model.embedded.*;
import com.rohi.spring_mongo.global.entity.BaseAuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@Document(collection = "products")
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "brand_category_idx", def = "{'brandId' : 1, 'categoryId' : 1}"),
        @CompoundIndex(name = "price_rating_idx", def = "{'pricing.currentPrice' : 1, 'ratings.averageRating' : -1}"),
        @CompoundIndex(name = "sales_performance_idx", def = "{'salesMetrics.soldLastMonth' : -1, 'isActive' : 1}")
})
public class Product extends BaseAuditableEntity implements Serializable, Persistable<String> {
    @Id
    private String id;

    @Indexed
    private String name;

    @Indexed(unique = true)
    private String slug;

    private String description;
    private String shortDescription;

    @Indexed(unique = true)
    private String sku;

    @Indexed
    private String brandId; // Reference to Brand

    @Indexed
    private String categoryId; // Reference to Category

    private Pricing pricing;
    private Inventory inventory;
    private ProductAttributes attributes;
    private List<String> images;
    private Ratings ratings;
    private SEO seo;
    private SalesMetrics salesMetrics;

    @Indexed
    private Boolean isActive;
    private Boolean isFeatured;
    private List<String> tags;


    @Override
    public boolean isNew() {
        return this.id == null;
    }
}