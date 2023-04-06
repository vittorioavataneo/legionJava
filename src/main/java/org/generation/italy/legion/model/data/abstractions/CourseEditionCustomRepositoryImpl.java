package org.generation.italy.legion.model.data.abstractions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.generation.italy.legion.model.entities.CourseEdition;

import java.util.ArrayList;
import java.util.Optional;

import static org.generation.italy.legion.model.data.HibernateConstants.*;

public class CourseEditionCustomRepositoryImpl implements CourseEditionCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Iterable<CourseEdition> findMedian() {
        TypedQuery<Long> qCountCE = em.createQuery(HQL_COUNT_COURSE_EDITION, Long.class);
        int countCe= (int) qCountCE.getSingleResult().longValue();
        if(countCe == 0){
            return new ArrayList<CourseEdition>();
        }
        TypedQuery<CourseEdition> qAllCourseEdition = em.createQuery(HQL_GET_ALL_COURSE_EDITION, CourseEdition.class);
        if(countCe % 2 != 0){
            return qAllCourseEdition.setFirstResult(countCe / 2).setMaxResults(1).getResultList();
        }
        return qAllCourseEdition.setFirstResult(countCe / 2 - 1).setMaxResults(2).getResultList();
    }

    @Override
    public Optional<Double> findCourseEditionCostMode() {
        TypedQuery<Double> q = em.createQuery(HQL_FIND_MODE_COURSE_EDITION_COST, Double.class);
        return q.setMaxResults(1).getResultList().stream().findFirst();
    }
}
