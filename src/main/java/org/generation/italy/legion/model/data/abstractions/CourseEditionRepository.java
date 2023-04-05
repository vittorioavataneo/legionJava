package org.generation.italy.legion.model.data.abstractions;

import org.generation.italy.legion.model.entities.CourseEdition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseEditionRepository extends JpaRepository<CourseEdition, Long> {

    CourseEdition findById(long courseEditionId);
    List<CourseEdition> findAll();
    double findTotalCost();
    Optional<CourseEdition> findMostExpensive();
    double findAverageCost();
    Iterable<Double> findAllDurations();
    Iterable<CourseEdition> findCourseEditionsByCourse(long courseId);

    @Query("""
            SELECT ce
            FROM CourseEdition as ce
            JOIN Course as c
            USING (id)
            WHERE (c.title LIKE :titlePart) AND (ce.startedAt BETWEEN :startAt AND :endAt)
            """
    )
    Iterable<CourseEdition> findCourseEditionsByCourseTitleAndPeriod(String titlePart,
                                                                     LocalDate startAt, LocalDate endAt);
    @Query("""
            SELECT ce
            FROM CourseEdition ce
            ORDER BY ce.cost
            SKIP
               CASE
                 WHEN (SELECT COUNT(*) FROM CourseEdition) % 2 = 0 THEN (COUNT(ce)/2)-1
                 ELSE FLOOR(COUNT(ce)/2)
               END;
            LIMIT
               CASE
                 WHEN (SELECT COUNT(*) FROM CourseEdition) % 2 = 0 THEN 2
                 ELSE 1
               END;
            """)
    Iterable<CourseEdition> findCourseEditionsMedian();

    @Query("""
            SELECT ce FROM CourseEdition ce
            WHERE ce.cost = (
              SELECT MODE(ce2.cost) FROM CourseEdition ce2
            )
            """)
    Optional<Double> findCourseEditionCostMode();

}