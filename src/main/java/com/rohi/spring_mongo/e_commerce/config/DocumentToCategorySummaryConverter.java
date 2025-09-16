package com.rohi.spring_mongo.e_commerce.config;

import com.mongodb.lang.NonNull;
import com.rohi.spring_mongo.e_commerce.dto.view.CategorySummary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.bson.Document;


@ReadingConverter
public class DocumentToCategorySummaryConverter implements Converter<Document, CategorySummary> {

    @Override
    public CategorySummary convert(@NonNull Document source) {
        return CategorySummary.builder()
                .id(source.getString("id"))
                .name(source.getString("name"))
                .slug(source.getString("slug"))
                .parentCategory(source.getString("parentCategory") != null ? source.getString("parentCategory") : null)
                .build();
    }
}