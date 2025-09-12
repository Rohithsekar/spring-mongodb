package com.rohi.spring_mongo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddAuthorRequest {

    @NotNull(message = "name cannot be null")
    private String name;
    @Email
    @NotNull(message = "email cannot be null")
    private String email;
    @NotNull(message = "articleCount cannot be null")
    private Integer articleCount;
}
