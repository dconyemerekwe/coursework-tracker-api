package com.courseworktrackerapi;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // listens to all controllers for any errors that get thrown
public class GlobalExceptionHandler {

  // tells Spring which method should handle which specific error
  @ExceptionHandler(CourseworkNotFoundException.class)
  public ResponseEntity<ApiExceptionPayload> handleCourseworkNotFoundException(
      CourseworkNotFoundException e) {

    ApiExceptionPayload payload =
        new ApiExceptionPayload(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now());

    return new ResponseEntity<>(payload, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidDateException.class)
  public ResponseEntity<ApiExceptionPayload> handleInvalidDateException(InvalidDateException e) {

    ApiExceptionPayload payload =
        new ApiExceptionPayload(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now());

    return new ResponseEntity<>(payload, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TitleAlreadyExistsException.class)
  public ResponseEntity<ApiExceptionPayload> handleTitleAlreadyExistsException(
      TitleAlreadyExistsException e) {

    ApiExceptionPayload payload =
        new ApiExceptionPayload(e.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now());

    return new ResponseEntity<>(payload, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiExceptionPayload> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    // handles many-to-many relationships (one field can trigger multiple validation rules)
    Map<String, List<String>> errors = new HashMap<>();

    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      String fieldName = fieldError.getField();
      String errorMessage = fieldError.getDefaultMessage();

      // retrieves the list for the field/creates a new list if it's the first fail
      errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
    }

    ApiExceptionPayload payload =
        new ApiExceptionPayload(
            "Validation failed: Please correct the errors in your request.",
            errors,
            HttpStatus.BAD_REQUEST,
            ZonedDateTime.now());

    return new ResponseEntity<>(payload, HttpStatus.NOT_FOUND);
  }
}
