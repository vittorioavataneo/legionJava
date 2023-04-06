package org.generation.italy.legion.dtos;

import org.generation.italy.legion.model.entities.CourseEdition;

import java.util.stream.StreamSupport;

public class SimpleCourseEditionDto {
    private long id;
    private double cost;

    public SimpleCourseEditionDto(long id, double cost) {
        this.id = id;
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public static SimpleCourseEditionDto fromEntity(CourseEdition ce){
        return new SimpleCourseEditionDto(ce.getId(), ce.getCost());
    }
    public static Iterable<SimpleCourseEditionDto> fromEntityIterable(Iterable<CourseEdition> iCe){
        return StreamSupport.stream(iCe.spliterator(), false)
                .map(SimpleCourseEditionDto::fromEntity)
                .toList();
    }
}
