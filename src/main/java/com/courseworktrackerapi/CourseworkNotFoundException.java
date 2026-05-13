package com.courseworktrackerapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseworkNotFoundException extends RuntimeException {
    public CourseworkNotFoundException(String message) {
        super(message);
    }
}
