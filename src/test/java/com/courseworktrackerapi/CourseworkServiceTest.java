package com.courseworktrackerapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseworkServiceTest {

    @Mock // injects a mock for an instance variable
    private CourseworkRepository courseworkRepository;

    @InjectMocks // mocked repository automatically injected into this service instance
    private  CourseworkService courseworkService;

    @Test
    void shouldReturnCourseworkResponseWhenIdExists() {
        // given
        Long courseworkId = 1L;
        Coursework coursework = new Coursework();
        coursework.setId(courseworkId);
        coursework.setTitle("Course 1");
        coursework.setModuleCode("TEST123");

        // mock must return an Optional containing the entity
        given(courseworkRepository.findById(courseworkId)).willReturn(Optional.of(coursework));

        // when
        // call the service method and store result
        CourseworkResponse result = courseworkService.getCourseworkById(courseworkId);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(coursework.getId(), result.id()),
                () -> assertEquals(coursework.getTitle(), result.title()),
                () -> assertEquals(coursework.getModuleCode(), result.moduleCode())
        );

        // verifying the service communicated with the repository
        then(courseworkRepository).should().findById(coursework.getId());
    }

    @Test
    void shouldThrowCourseWorkNotFoundExceptionWhenIdDoesNotExist() {
        // given
        Long courseworkId = 1L;

        // mock must return an empty Optional (entity not found)
        given(courseworkRepository.findById(courseworkId)).willReturn(Optional.empty());

        // when & then
        CourseworkNotFoundException exception = assertThrows(
                CourseworkNotFoundException.class,
                () ->
                    courseworkService.getCourseworkById(courseworkId));

        assertAll(
                () -> assertEquals("Coursework with id " + courseworkId + " not found",
                        exception.getMessage()));

        then(courseworkRepository).should().findById(courseworkId);
    }

    @Test
    void shouldReturnFilteredCourseworkByStatus() {
        // given
        Status status = Status.IN_PROGRESS;
        String moduleCode = null;
        Coursework coursework = new Coursework();
        coursework.setStatus(status);
        coursework.setTitle("Course 1");

        List<Coursework> testList = List.of(coursework);

        given(courseworkRepository.findByStatus(status)).willReturn(testList);

        // when
        List<CourseworkResponse> result = courseworkService.getCoursework(status, moduleCode);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.get(0).status()),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(testList.get(0).getStatus(), result.get(0).status()),
                () -> assertEquals(testList.get(0).getTitle(), result.get(0).title())
        );

        then(courseworkRepository).should().findByStatus(status);
        verify(courseworkRepository, never()).findAll();
        verify(courseworkRepository, never()).findByModuleCode(moduleCode);
        verify(courseworkRepository, never()).findByStatusAndModuleCode(status, moduleCode);
    }

    @Test
    void shouldReturnFilteredCourseworkByModuleCode() {
        // given
        Status status = null;
        String moduleCode = "TEST1234";
        Coursework coursework = new Coursework();
        coursework.setStatus(status);
        coursework.setModuleCode(moduleCode);
        coursework.setTitle("Course 1");

        List<Coursework> testList = List.of(coursework);

        given(courseworkRepository.findByModuleCode(moduleCode)).willReturn(testList);

        // when
        List<CourseworkResponse> result = courseworkService.getCoursework(status, moduleCode);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.get(0).moduleCode()),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(testList.get(0).getModuleCode(), result.get(0).moduleCode()),
                () -> assertEquals(testList.get(0).getTitle(), result.get(0).title())
        );

        then(courseworkRepository).should().findByModuleCode(moduleCode);
        verify(courseworkRepository, never()).findAll();
        verify(courseworkRepository, never()).findByStatus(status);
        verify(courseworkRepository, never()).findByStatusAndModuleCode(status, moduleCode);
    }

    @Test
    void shouldCallCombinedFilterWhenBothParamsProvided() {
        // given
        Status status = Status.IN_PROGRESS;
        String moduleCode = "TEST123";
        Coursework coursework = new Coursework();
        coursework.setStatus(status);
        coursework.setModuleCode(moduleCode);

        List<Coursework> testList = List.of(coursework);

        given(courseworkRepository.findByStatusAndModuleCode(status, moduleCode)).willReturn(testList);

        // when
        List<CourseworkResponse> result = courseworkService.getCoursework(status, moduleCode);

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(testList.get(0).getStatus(), result.get(0).status()),
                () -> assertEquals(testList.get(0).getModuleCode(), result.get(0).moduleCode()),
                () -> assertEquals(1, result.size())
        );

        then(courseworkRepository).should().findByStatusAndModuleCode(status, moduleCode);
        verify(courseworkRepository, never()).findByStatus(status);
        verify(courseworkRepository, never()).findByModuleCode(moduleCode);

    }

    @Test
    void shouldReturnEmptyListWhenNoCourseworkExists() {
        // given
        List<Coursework> testList = Collections.emptyList();

        given(courseworkRepository.findAll()).willReturn(testList);

        // when
        List<CourseworkResponse> result = courseworkService.getCoursework(null, null);

        // then
        assertAll(
                () -> assertNotNull(result), // not null but list
                () -> assertTrue(result.isEmpty()), // empty list
                () -> assertEquals(0, result.size()) // confirm count
        );

        then(courseworkRepository).should().findAll();
    }

    @Test
    void shouldCorrectlyTransformEntityToResponse() {
        // given
        Coursework coursework = new Coursework(
                1L,
                "Test 1",
                "TEST1234",
                LocalDate.of(2026, Month.MAY, 20),
                Status.SUBMITTED
        );

        given(courseworkRepository.findById(coursework.getId())).willReturn(Optional.of(coursework));

        // when
        CourseworkResponse result = courseworkService.getCourseworkById(coursework.getId());

        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(coursework.getId(), result.id()),
                () -> assertEquals(coursework.getTitle(), result.title()),
                () -> assertEquals(coursework.getModuleCode(), result.moduleCode()),
                () -> assertEquals(coursework.getDueDate(), result.dueDate()),
                () -> assertEquals(coursework.getStatus(), result.status())
        );
    }

    @Test
    void shouldThrowExceptionWhenRepositoryFails() {
        // given
        given(courseworkRepository.findAll()).willThrow(
                new RuntimeException("Database connection failed."));

        // when & then
        assertThrows(RuntimeException.class,
                () -> courseworkService.getCoursework(null, null));
    }
}