package com.rohi.spring_mongo.e_commerce.dto.view;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetrics {
    private Long totalProducts;
    private Long totalOrders;
    private Long totalUsers;
    private Double totalRevenue;
    private Double monthlyRevenue;
    private Long lowStockProducts;
    private Double averageOrderValue;
    private Double averageRating;
}
