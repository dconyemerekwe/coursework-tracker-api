package com.courseworktrackerapi;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

// DTO layer (input)

// decouples the internal DB model from the external API model providing security & flexibility

// immutable type of class with automatic getters, equals() & toString()
// defines request rules to fail any bad requests to the controller early
public record CourseworkRequest(
    Status status,
    @FutureOrPresent LocalDate dueDate,
    @NotBlank String moduleCode,
    @NotBlank String title) {}
