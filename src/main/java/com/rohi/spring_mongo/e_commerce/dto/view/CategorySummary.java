package com.rohi.spring_mongo.e_commerce.dto.view;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummary {
    private String id;
    private String name;
    private String slug;
    private String parentCategory;
}
