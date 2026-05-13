package com.courseworktrackerapi;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;


class GlobalExceptionHandlerTest {

    @Test
    void shouldCorrectlyThrowCourseworkNotFoundException() {
        // given
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        CourseworkNotFoundException courseworkNotFoundException =
                new CourseworkNotFoundException("Coursework with ID 1 not found.");

        // when
        ResponseEntity<ApiExceptionPayload> result =
                globalExceptionHandler.handleCourseworkNotFoundException(courseworkNotFoundException);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getBody().timestamp()),
                () -> assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode()),
                () -> assertEquals(courseworkNotFoundException.getMessage(), result.getBody().message())
        );
    }

    @Test
    void shouldCorrectlyThrowInvalidDateException() {
        // given
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        InvalidDateException invalidDateException =
                new InvalidDateException("Due date cannot be before current date.");

        // when
        ResponseEntity<ApiExceptionPayload> result =
                globalExceptionHandler.handleInvalidDateException(invalidDateException);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getBody().timestamp()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> assertEquals(invalidDateException.getMessage(), result.getBody().message())
        );
    }

    @Test
    void shouldCorrectlyThrowTitleAlreadyExistsException() {
        // given
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        TitleAlreadyExistsException titleAlreadyExistsException =
                new TitleAlreadyExistsException("Coursework with title TEST1234 already exists.");

        // when
        ResponseEntity<ApiExceptionPayload> result =
                globalExceptionHandler.handleTitleAlreadyExistsException(titleAlreadyExistsException);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getBody().timestamp()),
                () -> assertEquals(HttpStatus.CONFLICT, result.getStatusCode()),
                () -> assertEquals(titleAlreadyExistsException.getMessage(), result.getBody().message())
        );
    }

    @Test
    void shouldCorrectlyThrowMethodArgumentNotValidException() {
        // given
        BindingResult bindingResult = mock(BindingResult.class);

        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        String titleErrorMessage = "Title is required.";
        String moduleCodeErrorMessage = "Module code is required.";
        List<FieldError> fieldErrors = List.of(
                new FieldError("coursework", "title", titleErrorMessage),
                new FieldError("coursework", "moduleCode", moduleCodeErrorMessage)
        );

        given(bindingResult.getFieldErrors()).willReturn(fieldErrors);

        MethodArgumentNotValidException methodArgumentNotValidException =
                new MethodArgumentNotValidException(null, bindingResult);

        // when
        ResponseEntity<ApiExceptionPayload> result =
                globalExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        // then
        assertAll(
                () -> assertNotNull(result),

                () -> assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, result.getBody().httpStatus()),

                () -> assertEquals("Validation failed: Please correct the errors in your request.",
                        result.getBody().message()),

                () -> assertNotNull(result.getBody().errors()),
                () -> assertEquals(2, result.getBody().errors().size()),

                () -> assertTrue(result.getBody().errors().containsKey("title")),
                () -> assertEquals(titleErrorMessage,
                        result.getBody().errors().get("title").get(0)),

                () -> assertTrue(result.getBody().errors().containsKey("moduleCode")),
                () -> assertEquals(moduleCodeErrorMessage,
                        result.getBody().errors().get("moduleCode").get(0))
        );

    }
}