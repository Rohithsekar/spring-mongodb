package com.rohi.spring_mongo.repository;

import com.rohi.spring_mongo.entity.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {

    Optional<Author> findByEmail(String email);

    // MongoDB logical operator. It's used to perform a search that matches documents satisfying any of the expressions within its array.
    //$regex: This is the MongoDB operator for regular expression matching.
    //?0: This is a placeholder for the parameters passed into the method. Spring Data maps the first parameter
    // (searchString) to the ?0 placeholder. If there were a second parameter, it would be ?1, and so on.
    //$options: 'i': This is an option for the $regex operator. The 'i' stands for case-insensitivity,
    @Query("{$or : [{ 'name' : { $regex : ?0, $options: 'i' } }, { 'email' : { $regex : ?0, $options: 'i' } }]}")
    List<Author> findByNameOrEmailContaining(String searchString, Pageable pageable);

    // New method to get the count of matching authors
    @Query(value = "{$or : [{ 'name' : { $regex : ?0, $options: 'i' } }, { 'email' : { $regex : ?0, $options: 'i' } }]}", count = true)
    long countByNameOrEmailContaining(String searchString);

    long countByName(String name);
    List<Author> findByName(String name, Pageable pageable);

    @Query("{ 'articleCount' : { $gte : ?0 } }")
    List<Author> findAuthorsGreaterThan(Integer articlesCount, Pageable pageable);

    @Query(value ="{ 'articleCount' : { $gte : ?0 } }", count = true)
    long countAuthorsGreaterThan(Integer articlesCount);

}
