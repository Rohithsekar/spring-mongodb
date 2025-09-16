package com.rohi.spring_mongo.e_commerce.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.io.Serializable;


// Rating Distribution embedded document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDistribution implements Serializable {
    private Integer five;
    private Integer four;
    private Integer three;
    private Integer two;
    private Integer one;
}