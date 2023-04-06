package org.generation.italy.legion.restControllers;

import org.generation.italy.legion.dtos.CourseDto;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.entities.Course;
import org.generation.italy.legion.model.services.abstractions.AbstractCrudService;
import org.generation.italy.legion.model.services.abstractions.AbstractDidacticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/courses")
public class ApiCourseController {
    private AbstractDidacticService didacticService;
    private AbstractCrudService<Course> courseService;

    @Autowired
    public ApiCourseController(AbstractDidacticService didacticService, AbstractCrudService<Course> courseService){

        this.didacticService = didacticService;
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> findById(@PathVariable long id){
        try {
            Optional<Course> c = courseService.findById(id);
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
    public ResponseEntity<Iterable<CourseDto>> findCoursesByTitleContainsActiveAndMinEdition(@RequestParam() String part,
                                                                          @RequestParam(required = false) Boolean active,
                                                                          @RequestParam(required = false) Integer minEdition){
        try {
            if(active == null && minEdition == null) {
                Iterable<Course> courseIterable = didacticService.findCoursesByTitleContains(part);
                return ResponseEntity.ok().body(CourseDto.fromEntityIterable(courseIterable));
            }else if(minEdition == null){
                Iterable<Course> courseIterable = didacticService.findCoursesByTitleAndActive(part, active);
                return ResponseEntity.ok().body(CourseDto.fromEntityIterable(courseIterable));
            }else if(part!=null){
                Iterable<Course> courseIterable = didacticService.findCoursesByTitleActiveAndMinEditions(part, active, minEdition);
                return ResponseEntity.ok().body(CourseDto.fromEntityIterable(courseIterable));
            }else {
                return ResponseEntity.badRequest().build();
            }
        } catch (DataException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


}

