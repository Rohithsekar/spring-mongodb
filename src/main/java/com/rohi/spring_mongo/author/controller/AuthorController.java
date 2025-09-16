package com.rohi.spring_mongo.author.controller;

import com.rohi.spring_mongo.global.constants.Constants;
import com.rohi.spring_mongo.author.dto.request.AddAuthorRequest;
import com.rohi.spring_mongo.author.dto.request.UpdateAuthorRequest;
import com.rohi.spring_mongo.author.dto.response.APIResponse;
import com.rohi.spring_mongo.author.dto.response.AuthorResponse;
import com.rohi.spring_mongo.author.service.AuthorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/author")
@Validated
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<APIResponse> addAuthors(@RequestBody @Valid List<AddAuthorRequest> addAuthorRequestList, HttpServletRequest servletRequest) {
        authorService.addAuthors(addAuthorRequestList, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse(Constants.SUCCESS, "Author records persisted successfully", new ArrayList<>()));
    }

    @GetMapping
    public ResponseEntity<APIResponse> fetchAuthors(@RequestParam(required = false) String key,
                                                    @RequestParam(required = false) String value,
                                                    @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                    @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                    @RequestParam(required = false, defaultValue = "createdDate") String sortField,
                                                    @RequestParam(required = false, defaultValue = "desc") String order,
                                                    @RequestParam(required = false) String searchFor,
                                                    HttpServletRequest servletRequest) {
        AuthorResponse authorResponse = authorService.fetchAllAuthors(key, value, pageNo, pageSize, sortField, order, searchFor, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(Constants.SUCCESS, "Author records fetched successfully", authorResponse));
    }

    @PutMapping
    public ResponseEntity<APIResponse> updateAuthor(@RequestBody @Valid UpdateAuthorRequest updateAuthorRequest, HttpServletRequest servletRequest) {
        authorService.updateAuthor(updateAuthorRequest, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(Constants.SUCCESS, "Author record updated successfully", new ArrayList<>()));
    }

    @DeleteMapping
    public ResponseEntity<APIResponse> deleteAuthor(@RequestParam String id, HttpServletRequest servletRequest) {
        authorService.deleteAuthor(id, servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(Constants.SUCCESS, "Author record deleted successfully", new ArrayList<>()));
    }
}
