package com.courseworktrackerapi;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public record ApiExceptionPayload(
        String message,
        Map<String, List<String>> errors,
        HttpStatus httpStatus,
        ZonedDateTime timestamp) {

    // compact constructor for single error exceptions
    public ApiExceptionPayload(
            String message,
            HttpStatus httpStatus,
            ZonedDateTime timestamp) {
        this(message, null, httpStatus, timestamp);
    }
}


