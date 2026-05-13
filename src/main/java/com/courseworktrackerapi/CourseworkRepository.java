package com.courseworktrackerapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// manages Coursework entities and their PKs using standard DB operations
public interface CourseworkRepository extends JpaRepository<Coursework, Long> {

    // automatically converts into a SQL query by Spring Data JPA
    Optional<Coursework> findByTitle(String title);

    List<Coursework> findByStatus(Status status);

    List<Coursework> findByModuleCode(String moduleCode);

    List<Coursework> findByStatusAndModuleCode(Status status, String moduleCode);
}
