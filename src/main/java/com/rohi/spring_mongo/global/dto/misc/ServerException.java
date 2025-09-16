package com.rohi.spring_mongo.global.dto.misc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class ServerException extends RuntimeException {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    //server exception is the generic exception that wraps all 500 server error
    public ServerException(String message, String path, HttpStatus status) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

}
