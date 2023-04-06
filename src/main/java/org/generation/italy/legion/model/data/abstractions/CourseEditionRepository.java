package org.generation.italy.legion.model.data.abstractions;

import org.generation.italy.legion.model.entities.CourseEdition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface CourseEditionRepository extends GenericRepository<CourseEdition>, CourseEditionCustomRepository {

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

    Iterable<CourseEdition> findMedian();
    Optional<Double> findCourseEditionCostMode();

}