package com.courseworktrackerapi;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// web layer

@RestController // tells Spring this class handles web requests & returns data
@RequestMapping(path = "/api/v1/coursework") // sets base URL
public class CourseworkController {

    // holds the required DB dependency
    private final CourseworkService courseworkService;

    // constructor injection: Spring automatically provides the bean
    @Autowired
    public CourseworkController(CourseworkService courseworkService) {
        this.courseworkService = courseworkService;
    }

    // GET (retrieves/reads data)
    @GetMapping
    public List<CourseworkResponse> getCoursework(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String moduleCode) {
        return courseworkService.getCoursework(status, moduleCode);
    }

    @GetMapping(path = "/{courseworkId}")
    public CourseworkResponse getCourseworkById(
            @PathVariable Long courseworkId) {

        return courseworkService.getCourseworkById(courseworkId);
    }

    // POST (creates data)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // returns HTTP 201 created
    // takes incoming JSON from user and de-serializes it into DTO
    public void addCoursework(@RequestBody CourseworkRequest courseworkRequest) {
        courseworkService.addNewCoursework(courseworkRequest);
    }

    // DELETE (removes data)
    @DeleteMapping(path = "/{courseworkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // returns HTTP 204 successful no content
    public void deleteCoursework(@PathVariable Long courseworkId) {
        courseworkService.deleteCoursework(courseworkId);
    }


    // PUT (modifies existing data)
    @PutMapping(path = "/{courseworkId}")
    // wrapper that allows to control HTTP status codes with data
    public ResponseEntity<CourseworkResponse> updateCoursework(
            @PathVariable Long courseworkId,
            @Valid @RequestBody CourseworkRequest courseworkRequest) {

        return ResponseEntity.ok(courseworkService.updateCoursework(courseworkId, courseworkRequest));
    }
}
