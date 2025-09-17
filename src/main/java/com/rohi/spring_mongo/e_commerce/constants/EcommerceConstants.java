package com.rohi.spring_mongo.e_commerce.constants;


public class EcommerceConstants {

    public static final String RATINGS = "ratings";
    public static final String AVERAGE_RATING = "averageRating";
    public static final String NESTED_AVERAGE_RATING = RATINGS + "." + AVERAGE_RATING;
    //salesMetrics
    public static final String SALES_METRICS = "salesMetrics";
    public static final String SOLD_LAST_MONTH = "soldLastMonth";
    public static final String NESTED_SOLD_LAST_MONTH = SALES_METRICS + "." + SOLD_LAST_MONTH;

    //discountPercentage
    public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
    public static final String PRICING = "pricing";
    public static final String NESTED_DISCOUNT_PERCENTAGE = PRICING + "." + DISCOUNT_PERCENTAGE;
}
