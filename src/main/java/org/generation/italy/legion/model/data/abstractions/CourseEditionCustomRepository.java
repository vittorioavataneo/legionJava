package org.generation.italy.legion.model.data.abstractions;

import org.generation.italy.legion.model.entities.CourseEdition;

import java.util.Optional;

public interface CourseEditionCustomRepository {
    Iterable<CourseEdition> findMedian();
    Optional<Double> findCourseEditionCostMode();
}
