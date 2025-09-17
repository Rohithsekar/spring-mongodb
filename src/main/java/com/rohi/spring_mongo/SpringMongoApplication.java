package com.rohi.spring_mongo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rohi.spring_mongo.author.model.Author;
import com.rohi.spring_mongo.author.repository.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Spring Data MongoDB provides a high-level abstraction over the MongoDB Query API and
 * simplifies integration of MongoDB into our application.
 */

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"com.rohi.spring_mongo.author.repository",
        "com.rohi.spring_mongo.e_commerce.repository"})
public class SpringMongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMongoApplication.class, args);

    }

    @Bean
    public CommandLineRunner run(AuthorRepository authorRepository) {
        return args -> {};
    }


}






