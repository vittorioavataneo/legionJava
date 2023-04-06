package org.generation.italy.legion.model.services.implementations;

import org.generation.italy.legion.model.data.abstractions.CourseEditionRepository;
import org.generation.italy.legion.model.data.abstractions.CourseRepository;
import org.generation.italy.legion.model.data.abstractions.TeacherRepository;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.entities.Course;
import org.generation.italy.legion.model.entities.CourseEdition;
import org.generation.italy.legion.model.entities.Level;
import org.generation.italy.legion.model.entities.Teacher;
import org.generation.italy.legion.model.services.abstractions.AbstractDidacticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StandardDidacticService implements AbstractDidacticService {
    //@Autowired
    private CourseRepository courseRepo; // field injection = inietta sul campo
    private TeacherRepository teacherRepo;
    private CourseEditionRepository editionRepo;
    @Autowired
    public StandardDidacticService(CourseRepository courseRepo, TeacherRepository teacherRepo, CourseEditionRepository editionRepo) { // constructor injection = inietta sul costruttore (è opzionale l'annotazione)
        this.courseRepo = courseRepo;       //iniezione delle dipendenze (tecnica) -> inversione del controllo (design pattern) o inversione delle dipendenze
        this.teacherRepo = teacherRepo;
        this.editionRepo = editionRepo;
    }


//COURSE
    @Override
    public List<Course> findCoursesByTitleContains(String part) throws DataException {
        return courseRepo.findByTitleContains(part);
    }

    @Override
    public boolean adjustActiveCourses(int numActive) throws DataException {
        //chiama il repository per scoprire quanti corsi attivi esistono
        //se i corsi attivi sono <= numActive ritorniamo false per segnalare
        //che non è stato necessario apportare alcuna modifica
        //altrimenti chiameremo un metodo sul repository che cancella gli
        //n corsi più vecchi
        int actives = courseRepo.countActiveCourses();
        if (actives <= numActive) {
            return false;
        }
        courseRepo.deactivateOldest(actives - numActive);
        return true;
    }

    @Override
    public Iterable<Course> findCoursesByTitleActiveAndMinEditions(String part, boolean active, int minEditions) {
        return courseRepo.findByTitleActiveAndMinEditions(part, active, minEditions);
    }

    @Override
    public Iterable<Course> findCoursesByTitleAndActive(String part, boolean active) {
        return courseRepo.findByTitleAndActive(part, active);
    }


//TEACHER
    @Override
    public Iterable<Teacher> findWithCompetenceByLevel(Level teacherLevel) throws DataException {
        return teacherRepo.findByLevel(teacherLevel);
    }

    @Override
    public Iterable<Teacher> findWithSkillAndLevel(long idSkill, Level competenceLevel) {
        return teacherRepo.findWithSkillAndLevel(idSkill, competenceLevel);
    }


//COURSE EDITION
    @Override
    public double findCourseEditionTotalCost() {

        return editionRepo.findTotalCost();
    }

    @Override
    public Optional<CourseEdition> findMostExpensiveCourseEdition() {

        return editionRepo.findFirstByOrderByCostDesc();
    }

    @Override
    public double findCourseEditionAverageCost() {

        return editionRepo.findAverageCost();
    }

    @Override
    public double findCourseEditionAllDurations() {
        return editionRepo.findAllDurations();
    }

    @Override
    public Iterable<CourseEdition> findCourseEditionsByCourse(long courseId) {
        return editionRepo.findByCourseId(courseId);
    }

    @Override
    public Iterable<CourseEdition> findCourseEditionsByCourseTitleAndPeriod(String titlePart, LocalDate startAt, LocalDate endAt) {
        return editionRepo.findByCourseTitleContainingAndStartedAtBetween(titlePart, startAt, endAt);
    }

    @Override
    public Iterable<CourseEdition> findAllCourseEdition() {
        return editionRepo.findAll();
    }

    @Override
    public Iterable<CourseEdition> findCourseEditionMedianByCost() {
        return editionRepo.findMedian();
    }

    @Override
    public Optional<Double> findCourseEditionCostMode() {
        return editionRepo.findCourseEditionCostMode();
    }


}
