package com.rohi.spring_mongo.service;

import com.rohi.spring_mongo.dto.request.AddAuthorRequest;
import com.rohi.spring_mongo.entity.Author;
import com.rohi.spring_mongo.repository.AuthorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test") // Activates the 'application-test.yml' configuration
class AuthorServiceIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setup() {
        // Clean up the database before each test to ensure isolation
        authorRepository.deleteAll();
    }

    @Test
    void whenAddAuthors_thenAuthorsAreSavedToMongoDb() {
        // Arrange
        AddAuthorRequest authorRequest = new AddAuthorRequest("Jane Doe", "jane.doe@example.com", 10);
        List<AddAuthorRequest> requestList = Collections.singletonList(authorRequest);

        // Act
        authorService.addAuthors(requestList, null); // Pass null for path in a simplified test

        // Assert
        List<Author> authorsInDb = authorRepository.findAll();
        assertThat(authorsInDb).hasSize(1);
        assertThat(authorsInDb.get(0).getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    void whenAddAuthorsWithDuplicateEmail_thenShouldThrowDuplicateKeyException() {
        // Arrange
        AddAuthorRequest authorRequest1 = new AddAuthorRequest("Jane Doe", "jane.doe@example.com", 10);
        AddAuthorRequest authorRequest2 = new AddAuthorRequest("John Smith", "jane.doe@example.com", 15);
        List<AddAuthorRequest> requestList = List.of(authorRequest1, authorRequest2);

        // Act & Assert
        try {
            authorService.addAuthors(requestList, null);
        } catch (Exception e) {
            // Assert that the exception is a data access exception related to a duplicate key.
            assertTrue(e.getMessage().contains("E11000 duplicate key error"));
        }
        
        // Ensure no authors were saved
        List<Author> authorsInDb = authorRepository.findAll();
        assertTrue(authorsInDb.isEmpty());
    }
}