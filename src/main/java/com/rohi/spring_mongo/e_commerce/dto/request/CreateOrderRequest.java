package com.rohi.spring_mongo.e_commerce.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "User ID is required")
    private String userId;

    @Valid
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequest> items;

    @Valid
    @NotNull(message = "Shipping address is required")
    private AddressRequest shippingAddress;
    
    @Valid
    private AddressRequest billingAddress;
    
    private String discountCode;
    private String paymentMethod;
    private String notes;
}