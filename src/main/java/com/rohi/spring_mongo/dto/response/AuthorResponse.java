package com.rohi.spring_mongo.dto.response;


import com.rohi.spring_mongo.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private Long count;
    private List<Author> authors;
}
