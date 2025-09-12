package com.rohi.spring_mongo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuthorRequest extends AddAuthorRequest{

    @NotNull(message = "id cannot be null")
    private String id;
}
