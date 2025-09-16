package com.rohi.spring_mongo.e_commerce.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryResponse {
    private String id;
    private String orderNumber;
    private String status;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private Integer itemCount;
    private String shippingStatus;
    private String trackingNumber;
}