package com.rohi.spring_mongo.e_commerce.model;

import com.rohi.spring_mongo.e_commerce.model.embedded.Location;
import com.rohi.spring_mongo.global.entity.BaseAuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

// User/Customer Document
@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "email_active_idx", def = "{'email' : 1, 'isActive' : 1}"),
        @CompoundIndex(name = "location_membership_idx", def = "{'location.country' : 1, 'membershipLevel' : 1}")
})
public class User extends BaseAuditableEntity implements Serializable, Persistable<String> {
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private Location location;

    @Indexed
    private String membershipLevel; // basic, premium, vip

    private LocalDateTime joinDate;
    private Double totalSpent;
    private Integer loyaltyPoints;
    private List<String> preferences;
    private Boolean isActive;


    @Override
    public boolean isNew() {
        return this.id == null;
    }
}