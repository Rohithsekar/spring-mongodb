package com.rohi.spring_mongo.e_commerce.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.io.Serializable;

// Product Attributes embedded document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributes implements Serializable {
    private String color;
    private String storage;
    private String screenSize;
    private String weight;
    private String operatingSystem;
    private String size;
    private String material;
    private String style;
    private String gender;
    private String memory;
    private String processor;
}