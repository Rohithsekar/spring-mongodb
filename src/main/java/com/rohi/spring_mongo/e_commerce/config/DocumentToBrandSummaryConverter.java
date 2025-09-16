package com.rohi.spring_mongo.e_commerce.config;

import com.mongodb.lang.NonNull;
import com.rohi.spring_mongo.e_commerce.dto.view.BrandSummary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.bson.Document;


@ReadingConverter
public class DocumentToBrandSummaryConverter implements Converter<Document, BrandSummary> {

    @Override
    public BrandSummary convert(@NonNull Document source) {
        return BrandSummary.builder()
                .id(source.getString("id"))
                .name(source.getString("name"))
                .slug(source.getString("slug"))
                .build();
    }
}