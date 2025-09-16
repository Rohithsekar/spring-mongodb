package com.rohi.spring_mongo.service;

import com.rohi.spring_mongo.author.service.AuthorService;
import com.rohi.spring_mongo.global.dto.misc.ServerException;
import com.rohi.spring_mongo.author.dto.request.AddAuthorRequest;
import com.rohi.spring_mongo.author.repository.AuthorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;


import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * <p>
 *    @Mock -> Creates mock instance. Mock objects are stubs; they don't contain any real code.
 *    You must tell them what to do.
 *
 *    @InjectMocks -> Creates a real AuthorService instance and injects the defined mocks into its constructor.
 * </p>
 *
 */

@ExtendWith(MockitoExtension.class) //JUnit 5 way to enable Mockito annotations. It initializes the mocks automatically.
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AuthorService authorService;

    private List<AddAuthorRequest> requestList;

    //The setUp method runs before every single test case to ensure a clean state
    @BeforeEach
    void setup(){
        requestList = Collections.singletonList(new AddAuthorRequest("John Doe", "john.doe@example.com", 5));
    }

    // Test case for a successful scenario
    @Test
    void whenAddAuthors_withValidData_thenSaveAllIsCalled() {
        // Arrange step
        // Mockito "stubbing" that tells a mock what to return when a specific method is called.
        //in this case, when the authorRepository mock is invoked, we instruct it to return empty collection
        when(authorRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        // Act
        //invoke the original service call with the mocked instance beans
        authorService.addAuthors(requestList, httpServletRequest);

        // Assert your intended behaviour
        verify(authorRepository, times(1)).saveAll(anyList());
    }

    // Test case for a scenario where IllegalArgumentException is thrown
    @Test
    void whenAddAuthors_withInvalidData_thenThrowsServerException() {
        // Arrange
        String requestPath = "/api/v1/author";
        when(httpServletRequest.getRequestURI()).thenReturn(requestPath);
        doThrow(new IllegalArgumentException("Invalid data")).when(authorRepository).saveAll(anyList());

        // Act & Assert
        ServerException thrown = assertThrows(ServerException.class, () -> {
            authorService.addAuthors(requestList, httpServletRequest);
        });

        // Verify the properties of the thrown exception
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), thrown.getStatus());
        Assertions.assertEquals("An unexpected error occurred while saving author records.", thrown.getMessage());
        Assertions.assertEquals(requestPath, thrown.getPath());
        verify(authorRepository, times(1)).saveAll(anyList());
    }




}