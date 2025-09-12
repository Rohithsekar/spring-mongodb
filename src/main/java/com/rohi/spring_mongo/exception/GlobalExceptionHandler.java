package com.rohi.spring_mongo.exception;

import com.rohi.spring_mongo.constants.Constants;
import com.rohi.spring_mongo.dto.misc.BadRequestException;
import com.rohi.spring_mongo.dto.misc.ServerException;
import com.rohi.spring_mongo.dto.response.APIResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = exception.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, f -> f.getDefaultMessage() == null ? "Please check the type, format and validity" : f.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, "Invalid request", fieldErrors));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<APIResponse> handleApplicationServerException(ServerException exception) {
        return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, "We are facing an issue.Please try again later", exception));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponse> handleBadRequestException(BadRequestException exception) {
        return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, "Invalid input", exception.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<APIResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, "Check request param", exception.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {


        Map<String, String> errors = new HashMap<>();
        // Iterate through the constraint violations
        ex.getConstraintViolations().forEach(violation -> {
            // Dynamically extract the field name from the property path
            String field = getFieldNameFromPath(violation);
            String message = violation.getMessage();
            errors.put(field, message);
        });

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", new java.util.Date());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Validation Failed");
        responseBody.put("message", "One or more fields in the request are invalid.");
        responseBody.put("details", errors);
        responseBody.put("path", request.getDescription(false).substring(4));

        return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, "One or more fields in the request are invalid.", responseBody));
    }

    // Helper method to get the field name
    private String getFieldNameFromPath(ConstraintViolation<?> violation) {
        Iterator<Path.Node> pathIterator = violation.getPropertyPath().iterator();
        Path.Node lastNode = null;
        while (pathIterator.hasNext()) {
            lastNode = pathIterator.next();
        }
        // The last node's name is the field name
        return (lastNode != null) ? lastNode.getName() : "unknownField";
    }


}
