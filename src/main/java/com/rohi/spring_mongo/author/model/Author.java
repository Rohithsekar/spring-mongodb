package com.rohi.spring_mongo.author.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rohi.spring_mongo.global.entity.BaseAuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Getter
@Setter
@Document(collection = "authors")
@CompoundIndex(def = "{'name': 1, 'email': 1}")
public class Author extends BaseAuditableEntity implements Serializable, Persistable<String> {

    @Id
    private String id;

    @Field(name = "full_name")
    private String name;

    @Indexed(unique = true)
    private String email;

    @Field(name = "article_count")
    private Integer articleCount;

    private String nationality;
    private Boolean active;

    //cannot be instantiated directly. Should be created via builder pattern
    private Author(String name, String email, Integer articleCount, Boolean active) {
        this.name = name;
        this.email = email;
        this.articleCount = articleCount;
        this.active = active == null || active;

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String email;
        private Integer articleCount;
        private Boolean active;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder articleCount(Integer articleCount) {
            this.articleCount = articleCount;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Author build() {
            return new Author(this.name, this.email, this.articleCount, this.active);
        }

    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        //when loaded from database or other source, the isNew field will be false
        //Only during object creation, it is set to true
        System.out.println("called");
        return this.id==null;
    }

}