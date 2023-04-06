package org.generation.italy.legion.model.data.abstractions;

import org.generation.italy.legion.model.entities.CourseEdition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface CourseEditionRepository extends JpaRepository<CourseEdition, Long>, CourseEditionCustomRepository {

    @Query("""
            select sum(ce.cost)
            from CourseEdition ce
            """)
    double findTotalCost();

    Optional<CourseEdition> findFirstByOrderByCostDesc();
    @Query ("""
            select avg(ce.cost)
            from CourseEdition ce
            """)
    double findAverageCost();

    @Query("""
            select sum(ce.course.duration)
            from CourseEdition ce
            """)
    double findAllDurations();

    Iterable<CourseEdition> findByCourseId(long courseId);

    Iterable<CourseEdition> findByCourseTitleContainingAndStartedAtBetween(String titlePart,
                                                                    LocalDate startAt, LocalDate endAt);
    /*       @Query("""
            select ce
            from CourseEdition ce
            order by ce.cost
            skip
               case
                 when (SELECT COUNT(ce) FROM CourseEdition) % 2 = 0 THEN (COUNT(ce)/2)-1
                 else FLOOR(COUNT(ce)/2)
               end;
            LIMIT
               CASE
                 WHEN (SELECT COUNT(ce) FROM CourseEdition) % 2 = 0 THEN 2
                 ELSE 1
               END;
            """)
    Iterable<CourseEdition> findCourseEditionsMedian();

    @Query("""
            SELECT ce.cost FROM CourseEdition ce
            GROUP BY ce.cost
            ORDER BY ce.cost DESC
            LIMIT 1
            """)
    Optional<Double> findCourseEditionCostMode();
    */
}