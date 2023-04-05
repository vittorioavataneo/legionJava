package org.generation.italy.legion.model.data.abstractions;

import org.generation.italy.legion.model.entities.CourseEdition;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseEditionRepositoryCustom {
    Iterable<CourseEdition> findCourseEditionsMedian();
    Optional<Double> findCourseEditionCostMode();
}
