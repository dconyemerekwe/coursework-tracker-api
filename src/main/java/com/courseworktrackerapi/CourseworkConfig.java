package com.courseworktrackerapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.Month;

@Configuration // establishes that this class contains 'Bean definitions' (instructions)
public class CourseworkConfig {

    @Bean // marks the method as the producer of a managed object
    @Profile("dev") // maps the Bean to a particular profile. Currently set as developer (for testing DB)
    // runs the code block after the app context is loaded
    CommandLineRunner commandLineRunner(CourseworkService courseworkService) {
        return args -> {
            CourseworkRequest courseworkRequest = new CourseworkRequest(
                Status.NOT_STARTED,
                LocalDate.of(2026, Month.MAY, 20),
                "COMP1749",
                "Java Backend Project"
            );

            courseworkService.addNewCoursework(courseworkRequest);
        };
    };
}
