package com.rohi.spring_mongo.e_commerce.model.embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipping implements Serializable{
    private String method; // standard, express, overnight
    private String carrier;
    private String trackingNumber;
    private java.time.LocalDateTime estimatedDelivery;
}