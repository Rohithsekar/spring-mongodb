package com.rohi.spring_mongo.e_commerce.model;

import com.rohi.spring_mongo.e_commerce.model.embedded.*;
import com.rohi.spring_mongo.global.entity.BaseAuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

// Order Document
@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
    @CompoundIndex(name = "user_status_date_idx", def = "{'userId' : 1, 'status' : 1, 'orderDate' : -1}"),
    @CompoundIndex(name = "status_date_idx", def = "{'status' : 1, 'orderDate' : -1}")
})
public class Order extends BaseAuditableEntity implements Serializable, Persistable<String> {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String orderNumber;

    @Indexed
    private String userId; // Reference to User
    
    @Indexed
    private String status; // pending, confirmed, shipped, delivered, cancelled, returned
    
    private String paymentStatus; // pending, paid, failed, refunded
    
    @Indexed
    private LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    
    private Address shippingAddress;
    private Address billingAddress;
    
    private List<OrderItem> items;
    private OrderPricing pricing;
    private Payment payment;
    private Shipping shipping;
    
    private String notes;

    @Override
    public boolean isNew() {
        return false;
    }


}