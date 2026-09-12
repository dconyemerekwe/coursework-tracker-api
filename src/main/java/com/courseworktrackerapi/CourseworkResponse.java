package com.courseworktrackerapi;

import java.time.LocalDate;

// DTO layer (output)

// decouples the internal DB model from the external API model providing security & flexibility

// immutable type of class with automatic getters, equals() & toString()
public record CourseworkResponse(
    Long id, Status status, LocalDate dueDate, String moduleCode, String title) {}
