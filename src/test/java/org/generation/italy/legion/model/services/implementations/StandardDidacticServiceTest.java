package org.generation.italy.legion.model.services.implementations;

import org.generation.italy.legion.model.data.abstractions.CourseRepository;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.entities.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StandardDidacticServiceTest {
    private CourseRepository repo;
    private StandardDidacticService service;
    private Course c1;
    private Course c2;
    private Course c3;
    private Course c4;
    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findCourseById() {
    }

    @Test
    void findCoursesByTitleContains() {
    }

    @Test
    void saveCourse() {
    }

    @Test
    void updateCourse() {
    }

    @Test
    void deleteCourseById() {
    }

    @Test
    void adjustActiveCourses_should_deactivate_oldest_courses_when_actives_are_more_than_desired() {
        try {
            assertTrue(service.adjustActiveCourses(2));
            assertFalse(c1.isActive());
            assertTrue(c2.isActive());
            assertFalse(c3.isActive());
            assertTrue(c4.isActive());
        } catch (DataException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void adjustActiveCourses_should_return_false_when_actives_already_less_than_desired() {
        try {
            boolean result = service.adjustActiveCourses(4);
            assertFalse(result);
            assertTrue(c1.isActive());
            assertTrue(c2.isActive());
            assertTrue(c3.isActive());
            assertTrue(c4.isActive());
        } catch (DataException e) {
            throw new RuntimeException(e);
        }
    }
}