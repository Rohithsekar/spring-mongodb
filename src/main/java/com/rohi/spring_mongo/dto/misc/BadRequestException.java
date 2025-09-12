package com.rohi.spring_mongo.dto.misc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BadRequestException extends RuntimeException {
    private LocalDateTime timestamp;
    private String message;
    private String path;

    public BadRequestException(String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.path = path;
    }
}
