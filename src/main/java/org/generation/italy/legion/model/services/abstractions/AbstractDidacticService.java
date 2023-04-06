package org.generation.italy.legion.model.services.abstractions;

import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.entities.Course;
import org.generation.italy.legion.model.entities.CourseEdition;
import org.generation.italy.legion.model.entities.Level;
import org.generation.italy.legion.model.entities.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AbstractDidacticService{

    List<Course> findCoursesByTitleContains(String part) throws DataException;

    boolean adjustActiveCourses(int numActive) throws DataException; //se corsi attivi > numActive disattiva i più vecchi

    Iterable<Course> findCoursesByTitleActiveAndMinEditions(String part, boolean active, int minEditions);
    Iterable<Course> findCoursesByTitleAndActive(String part, boolean active);
    Iterable<Teacher> findWithCompetenceByLevel(Level teacherLevel) throws DataException;
    Iterable<Teacher> findWithSkillAndLevel(long idSkill, Level competenceLevel) throws DataException;
    double findCourseEditionTotalCost();
    Optional<CourseEdition> findMostExpensiveCourseEdition();
    double findCourseEditionAverageCost();
    double findCourseEditionAllDurations();

    Iterable<CourseEdition> findCourseEditionsByCourse(long courseId);

    Iterable<CourseEdition> findCourseEditionsByCourseTitleAndPeriod(String titlePart,
                                                                     LocalDate startAt, LocalDate endAt);
    Iterable<CourseEdition> findAllCourseEdition();
    Iterable<CourseEdition> findCourseEditionMedianByCost();

    Optional<Double> findCourseEditionCostMode();
}