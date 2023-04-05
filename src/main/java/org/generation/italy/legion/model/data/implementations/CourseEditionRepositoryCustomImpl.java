package org.generation.italy.legion.model.data.implementations;

import org.generation.italy.legion.model.data.abstractions.CourseEditionRepositoryCustom;
import org.generation.italy.legion.model.entities.CourseEdition;

import java.util.Optional;

public class CourseEditionRepositoryCustomImpl implements CourseEditionRepositoryCustom {

    @Override
    public Iterable<CourseEdition> findCourseEditionsMedian() {
        return null;
    }

    @Override
    public Optional<Double> findCourseEditionCostMode() {
        return Optional.empty();
    }
}
