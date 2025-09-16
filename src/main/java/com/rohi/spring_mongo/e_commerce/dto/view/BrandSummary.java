package com.rohi.spring_mongo.e_commerce.dto.view;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandSummary {
    private String id;
    private String name;
    private String slug;
}