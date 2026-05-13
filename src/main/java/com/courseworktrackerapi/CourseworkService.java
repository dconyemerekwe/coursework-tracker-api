package com.courseworktrackerapi;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// service layer (business logic)

@Service // marks the class as a Spring-managed bean that contains 'business logic'
public class CourseworkService {

    // holds the required DB dependency
    private final CourseworkRepository courseworkRepository;

    // constructor injection: Spring automatically provides the bean
    @Autowired
    public CourseworkService(CourseworkRepository courseworkRepository) {
        this.courseworkRepository = courseworkRepository;
    }

    public List<CourseworkResponse> getCoursework(Status status, String moduleCode) {
        if (status != null && moduleCode != null) {
            return courseworkRepository.findByStatusAndModuleCode(status, moduleCode)
                    // processes collections through the memory reference
                    .stream().map(this::mapToResponse).toList();
        }

        if (status != null) {
            return courseworkRepository.findByStatus(status)
                    .stream().map(this::mapToResponse).toList();
        }

        if  (moduleCode != null) {
            return courseworkRepository.findByModuleCode(moduleCode)
                    .stream().map(this::mapToResponse).toList();
        }

        return courseworkRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public CourseworkResponse getCourseworkById(Long courseworkId) {

        return courseworkRepository.findById(courseworkId).map(this::mapToResponse).orElseThrow(
                () -> new CourseworkNotFoundException("Coursework with id " + courseworkId + " not found"));
        }

    public void addNewCoursework(CourseworkRequest courseworkRequest) {
        Coursework coursework = new Coursework();
        // container that may (not) have a value. Forcing to check if data exists before using
        Optional<Coursework> courseworkOptional =
                courseworkRepository.findByTitle(courseworkRequest.title());
        if (courseworkOptional.isPresent()) {
            throw new TitleAlreadyExistsException(
                    "Coursework with title " + courseworkRequest.title() + " already exists");
        }
        coursework.setStatus(courseworkRequest.status());
        coursework.setDueDate(courseworkRequest.dueDate());
        coursework.setModuleCode(courseworkRequest.moduleCode());
        coursework.setTitle(courseworkRequest.title());

        courseworkRepository.save(coursework);
    }

    public void deleteCoursework(Long id) {
        boolean exists = courseworkRepository.existsById(id);
        if (!exists) {
            throw new CourseworkNotFoundException(
                    "Coursework with ID " + id + " doesn't exist.");
        }

        courseworkRepository.deleteById(id);
    }

    @Transactional // ensures series of operations succeed/fail together
    public CourseworkResponse updateCoursework(
            Long id,
            CourseworkRequest courseworkRequest) {
        Coursework coursework = courseworkRepository.findById(id).orElseThrow(
                () -> new CourseworkNotFoundException(
                        "Coursework with ID " + id + " not found.")
        );

        if (courseworkRequest.status() != null && !Objects.equals(
                courseworkRequest.status(), coursework.getStatus())) {
            coursework.setStatus(courseworkRequest.status());
        }

        if (courseworkRequest.dueDate() != null) {
            if (courseworkRequest.dueDate().isBefore(LocalDate.now())) {
                throw new InvalidDateException("Due date cannot be before current date.");
            }
            coursework.setDueDate(courseworkRequest.dueDate());
        }

        if (courseworkRequest.moduleCode() != null && !courseworkRequest.moduleCode().isBlank()) {
            coursework.setModuleCode(courseworkRequest.moduleCode());
        }

        if (courseworkRequest.title() != null &&
                !courseworkRequest.title().isBlank() && !Objects.equals(
                        courseworkRequest.title(), coursework.getTitle())) {
            Optional<Coursework> courseworkOptional =
                    courseworkRepository.findByTitle(courseworkRequest.title());
            if (courseworkOptional.isPresent()) {
                throw new TitleAlreadyExistsException(
                        "Coursework with title " + courseworkRequest.title() + " already exists");
            }
            coursework.setTitle(courseworkRequest.title());
        }

        return mapToResponse(coursework);
    }

    private CourseworkResponse mapToResponse(Coursework coursework) {
        return new CourseworkResponse(
                coursework.getId(),
                coursework.getStatus(),
                coursework.getDueDate(),
                coursework.getModuleCode(),
                coursework.getTitle()
        );
    }
}


