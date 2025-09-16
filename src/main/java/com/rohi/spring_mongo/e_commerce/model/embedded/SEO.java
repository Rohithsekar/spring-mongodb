package com.rohi.spring_mongo.e_commerce.model.embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.io.Serializable;


// SEO embedded document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SEO implements Serializable {
    private String metaTitle;
    private String metaDescription;
    private java.util.List<String> keywords;
}