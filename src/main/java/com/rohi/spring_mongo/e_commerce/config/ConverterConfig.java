package com.rohi.spring_mongo.e_commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConverterConfig {

    @Bean
    @Primary // This ensures this bean takes precedence
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new DocumentToBrandSummaryConverter());
        converters.add(new DocumentToCategorySummaryConverter());
        return new MongoCustomConversions(converters);

    }
}