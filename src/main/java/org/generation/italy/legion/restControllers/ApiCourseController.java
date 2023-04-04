package org.generation.italy.legion.restControllers;

import org.generation.italy.legion.dtos.CourseDto;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.entities.Course;
import org.generation.italy.legion.model.services.abstractions.AbstractCourseDidacticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/courses")
public class ApiCourseController {
    private AbstractCourseDidacticService service;

    @Autowired
    public ApiCourseController(AbstractCourseDidacticService service){
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> findById(@PathVariable long id){
        try {
            Optional<Course> c = service.findById(id);
            if(c.isPresent()){
                return ResponseEntity.ok().body(CourseDto.fromEntity(c.get()));
            }
            return ResponseEntity.notFound().build();
        } catch (DataException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping()
    public ResponseEntity<Iterable<CourseDto>> findCoursesByTitleContainsActiveAndMinEdition(@RequestParam(required = true) String part,
                                                                          @RequestParam(required = false) Boolean active,
                                                                          @RequestParam(required = false) Integer minEdition){
        try {
            if(active == null && minEdition == null) {
                Iterable<Course> courseIterable = service.findCoursesByTitleContains(part);
                return ResponseEntity.ok().body(CourseDto.fromEntityIterable(courseIterable));
            }else if(minEdition == null){
                Iterable<Course> courseIterable = service.findCoursesByTitleAndActive(part, active);
                return ResponseEntity.ok().body(CourseDto.fromEntityIterable(courseIterable));
            }else {
                Iterable<Course> courseIterable = service.findCoursesByTitleActiveAndMinEditions(part, active, minEdition);
                return ResponseEntity.ok().body(CourseDto.fromEntityIterable(courseIterable));
            }
        } catch (DataException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }




}

