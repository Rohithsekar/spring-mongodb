package com.rohi.spring_mongo.e_commerce.model;


import com.rohi.spring_mongo.global.entity.BaseAuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;



@Document(collection = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "product_date_idx", def = "{'productId' : 1, 'date' : -1}"),
        @CompoundIndex(name = "date_period_idx", def = "{'date' : -1, 'period' : 1}")
})
public class Brand extends BaseAuditableEntity implements Serializable, Persistable<String> {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;
    
    @Indexed(unique = true)
    private String slug;
    
    private String description;
    private String logo;
    private String website;
    private Integer founded;
    private String country;
    private Boolean isActive;

    @Override
    public boolean isNew() {
        return this.id==null;
    }
}