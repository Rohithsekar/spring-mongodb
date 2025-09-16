package com.rohi.spring_mongo.e_commerce.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.rohi.spring_mongo.global.entity.BaseAuditableEntity;
import org.springframework.data.domain.Persistable;

import lombok.*;
import org.springframework.data.annotation.Id;



import java.io.Serializable;


@Document(collection = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseAuditableEntity implements Serializable, Persistable<String> {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    
    @Indexed(unique = true)
    private String slug;
    
    private String description;
    private String parentCategory; // Reference to parent category ID
    private Integer level;
    private Boolean isActive;


    @Override
    public boolean isNew() {
        return this.id==null;
    }

}