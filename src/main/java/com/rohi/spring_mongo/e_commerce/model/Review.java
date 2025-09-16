package com.rohi.spring_mongo.e_commerce.model;

import lombok.*;
import com.rohi.spring_mongo.global.entity.BaseAuditableEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "product_user_idx", def = "{'productId' : 1, 'userId' : 1}"),
        @CompoundIndex(name = "product_rating_date_idx", def = "{'productId' : 1, 'rating' : -1, 'createdAt' : -1}")
})
public class Review extends BaseAuditableEntity implements Serializable, Persistable<String> {

    @Id
    private String id;

    @Indexed
    private String productId; // Reference to Product

    @Indexed
    private String userId; // Reference to User

    private Integer rating;
    private String title;
    private String content;
    private List<String> pros;
    private List<String> cons;
    private Boolean isVerifiedPurchase;
    private Integer helpfulVotes;
    private Integer totalVotes;
    private String status; // pending, approved, rejected


    @Override
    public boolean isNew() {
        return this.id==null;
    }
}