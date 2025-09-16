package com.rohi.spring_mongo.e_commerce.dto.request;

import lombok.*;
import jakarta.validation.constraints.NotBlank;




@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;
    
    private String addressLine2;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotBlank(message = "Zip code is required")
    private String zipCode;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    @NotBlank(message = "Phone is required")
    private String phone;
}