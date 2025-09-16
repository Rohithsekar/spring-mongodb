package com.rohi.spring_mongo.e_commerce.model;

import com.rohi.spring_mongo.e_commerce.model.embedded.Demographics;
import com.rohi.spring_mongo.e_commerce.model.embedded.HistoricalMetrics;
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


// Sales History Document (Time-series data)
@Document(collection = "salesHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "product_date_idx", def = "{'productId' : 1, 'date' : -1}"),
        @CompoundIndex(name = "date_period_idx", def = "{'date' : -1, 'period' : 1}")
})
public class SalesHistory extends BaseAuditableEntity implements Serializable, Persistable<String> {
    @Id
    private String id;

    @Indexed
    private String productId; // Reference to Product

    @Indexed
    private LocalDateTime date;

    private String period; // daily, weekly, monthly
    private HistoricalMetrics metrics;
    private Demographics demographics;

    @Override
    public boolean isNew() {
        return false;
    }

}