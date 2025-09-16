package com.rohi.spring_mongo.author.service;


import com.mongodb.MongoException;
import com.rohi.spring_mongo.global.dto.misc.BadRequestException;
import com.rohi.spring_mongo.global.dto.misc.ServerException;
import com.rohi.spring_mongo.author.dto.request.AddAuthorRequest;
import com.rohi.spring_mongo.author.dto.request.UpdateAuthorRequest;
import com.rohi.spring_mongo.author.dto.response.AuthorResponse;
import com.rohi.spring_mongo.author.model.Author;
import com.rohi.spring_mongo.author.repository.AuthorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthorService {


    private final AuthorRepository authorRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     *
     * @param addAuthorRequestList request author records
     * @param servletRequest       container object consisting of the HTTP request properties
     * @author rohith sekar
     */
    public void addAuthors(List<AddAuthorRequest> addAuthorRequestList, HttpServletRequest servletRequest) {
        try {

            List<Author> authors = addAuthorRequestList.stream().map(a -> Author.builder()
                    .name(a.getName())
                    .email(a.getEmail())
                    .articleCount(a.getArticleCount())
                    .active(true)
                    .build()).toList();

            authorRepository.saveAll(authors);
            log.info("author records saved successfully");

        } catch (DuplicateKeyException e) {
            log.error("{} caught in addAuthors method: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BadRequestException("email already exists", servletRequest.getRequestURI());
        } catch (IllegalArgumentException e) {
            log.error("{} exception caught in addAuthors method : {}", e.getClass().getSimpleName(), e.getMessage());
            throw new ServerException("An unexpected error occurred while saving author records.", servletRequest.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param key            the filter field
     * @param value          the value for the filter field
     * @param pageNo         the page number param for pagination
     * @param pageSize       the page size param for pagination
     * @param sortField      the field to use for sorting records
     * @param order          the sort order (i.e, ascending, descending)
     * @param searchFor      the wildcard string to match
     * @param servletRequest container object consisting of the HTTP request properties
     * @return List<Author> matching author records
     * @author rohith sekar
     */
    public AuthorResponse fetchAllAuthors(String key, String value,
                                          Integer pageNo, Integer pageSize,
                                          String sortField, String order, String searchFor,
                                          HttpServletRequest servletRequest) {

        List<String> validQueryFields = List.of("name", "email", "articlesCount", "createdDate");
        validateQueryField(validQueryFields, sortField, servletRequest);
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending().and(Sort.by("id").descending()) : Sort.by(sortField).descending().and((Sort.by("id").descending()));

        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);

        if (searchFor != null) {
//            validateQueryField(validQueryFields, searchFor, servletRequest);
            Long count = authorRepository.countByNameOrEmailContaining(searchFor);
            List<Author> authors = authorRepository.findByNameOrEmailContaining(searchFor, pageRequest);
            return new AuthorResponse(count, authors);
        }

        if (key != null && value != null) {
            //query only for the provided field and value
            return fetchMatchingAuthors(key, value, pageRequest, servletRequest);
        } else {
            //no filters
            long count = authorRepository.count();
            List<Author> authors = authorRepository.findAll(pageRequest).getContent();
            return new AuthorResponse(count, authors);
        }

    }

    /**
     *
     * @param id             the unique identifier of the record
     * @param servletRequest container object consisting of the HTTP request properties
     * @author rohith sekar
     */
    public void deleteAuthor(String id, HttpServletRequest servletRequest) {
        try {
            authorRepository.deleteById(id);
            log.info("author record deleted successfully");
        } catch (OptimisticLockingFailureException e) {
            log.error("{} exception caught in deleteAuthor method: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BadRequestException("provided id does not exist", servletRequest.getRequestURI());
        }
    }

    /**
     *
     * @param updateAuthorRequest container object consisting of update fields along with object unique identifier
     * @param servletRequest      container object consisting of the HTTP request properties
     * @author rohith sekar
     */
    public void updateAuthor(UpdateAuthorRequest updateAuthorRequest, HttpServletRequest servletRequest) {
        try {
            String id = updateAuthorRequest.getId();

            Author existingAuthor = authorRepository.findById(id).orElseThrow(() -> {
                String error = "provided id doesn't exist";
                log.error(error);
                return new BadRequestException(error, servletRequest.getRequestURI());
            });

            existingAuthor.setName(updateAuthorRequest.getName());
            existingAuthor.setEmail(updateAuthorRequest.getEmail());
            existingAuthor.setArticleCount(updateAuthorRequest.getArticleCount());

            authorRepository.save(existingAuthor);
            log.info("author record updated successfully");

        } catch (MongoException e) {
            log.error("{} exception caught in updateAuthor method: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new ServerException("An unexpected error occurred while updating author record.", servletRequest.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private AuthorResponse fetchMatchingAuthors(String key, String value, PageRequest pageRequest, HttpServletRequest servletRequest) {

        String normalizedKey = key.toLowerCase().trim();
        long count = 0;
        List<Author> authors = new ArrayList<>();

        switch (normalizedKey) {
            case "articlescount":
                try {
                    int articlesCount = Integer.parseInt(value);
                    count = authorRepository.countAuthorsGreaterThan(articlesCount);
                    authors = authorRepository.findAuthorsGreaterThan(articlesCount, pageRequest);
                    return new AuthorResponse(count, authors);
                } catch (NumberFormatException e) {
                    String error = String.format("text input: %s for number field: %s", value, key);
                    log.error(error);
                    throw new BadRequestException(error, servletRequest.getRequestURI());
                }
            case "name":
                count = authorRepository.countByName(value);
                authors = authorRepository.findByName(value, pageRequest);
                return new AuthorResponse(count, authors);
            case "email":
                authors = authorRepository.findByEmail(value).map(List::of).orElse(Collections.emptyList());
                return new AuthorResponse(1L, authors);
            default:
                String error = String.format("Invalid query field: %s", key);
                log.error(error);
                throw new BadRequestException(error, servletRequest.getRequestURI());
        }
    }

    private void validateQueryField(List<String> validFields, String queryField, HttpServletRequest servletRequest) {

        validFields.stream().filter(v -> v.equals(queryField)).findAny().orElseThrow(() -> {
            String error = String.format("Invalid sort field: %s", queryField);
            log.error(error);
            return new BadRequestException(error, servletRequest.getRequestURI());
        });
    }
}
