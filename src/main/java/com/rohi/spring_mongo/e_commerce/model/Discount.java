package com.rohi.spring_mongo.e_commerce.model;

import com.rohi.spring_mongo.e_commerce.model.embedded.DiscountConditions;
import com.rohi.spring_mongo.e_commerce.model.embedded.DiscountUsage;
import com.rohi.spring_mongo.global.entity.BaseAuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Document(collection = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "code_active_idx", def = "{'code' : 1, 'isActive' : 1}"),
        @CompoundIndex(name = "validity_period_idx", def = "{'validFrom' : 1, 'validUntil' : 1}")
})
public class Discount extends BaseAuditableEntity implements Serializable, Persistable<String> {
    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private String name;
    private String description;
    private String discountType; // percentage, fixed_amount, buy_x_get_y
    private Double discountValue;

    private List<String> applicableProducts; // References to Product IDs
    private List<String> applicableCategories; // References to Category IDs

    private DiscountConditions conditions;

    @Indexed
    private LocalDateTime validFrom;

    @Indexed
    private LocalDateTime validUntil;

    private DiscountUsage usage;

    @Indexed
    private Boolean isActive;

    @Override
    public boolean isNew() {
        return this.id == null;
    }
}
