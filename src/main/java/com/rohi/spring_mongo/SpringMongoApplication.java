package com.rohi.spring_mongo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rohi.spring_mongo.entity.Author;
import com.rohi.spring_mongo.repository.AuthorRepository;
import org.instancio.Instancio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Spring Data MongoDB provides a high-level abstraction over the MongoDB Query API and
 * simplifies integration of MongoDB into our application.
 */

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rohi.spring_mongo.repository")
public class SpringMongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMongoApplication.class, args);

    }

    @Bean
    public CommandLineRunner run(AuthorRepository authorRepository) {

        return (args) -> {


            List<Author> authorList = authorRepository.findAll();

            //write those records to a json file using java.nio

            // Define the file path. We'll create a 'json_output' directory to keep things clean.
            String directoryPath = "json_output";
            String fileName = "authors.json";
            Path outputPath = Paths.get(directoryPath, fileName);

            // Create the directory if it does not exist
            try {
                Files.createDirectories(Paths.get(directoryPath));
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + directoryPath);
                e.printStackTrace();
                return; // Exit if directory creation fails
            }

            // Initialize the ObjectMapper to handle JSON serialization
            ObjectMapper mapper = new ObjectMapper();

            // Register the JavaTimeModule to handle Java 8 date/time types
            mapper.registerModule(new JavaTimeModule());

            // Use an ObjectWriter to pretty print the output for readability
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

            // Write the list of authors to the JSON file
            try {
                writer.writeValue(outputPath.toFile(), authorList);
                System.out.println("Successfully wrote authors to file: " + outputPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("An error occurred while writing the JSON file.");
                e.printStackTrace();
            }
        };
    }


//            authorList.forEach(a -> {
//                a.setCreatedBy("system-user");
//                a.setCreatedDate(LocalDateTime.now());
//            });


    }






