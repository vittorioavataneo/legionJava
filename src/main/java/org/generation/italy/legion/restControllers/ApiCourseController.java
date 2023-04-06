package org.generation.italy.legion.restControllers;

import org.generation.italy.legion.dtos.CourseDto;
import org.generation.italy.legion.model.data.abstractions.GenericRepository;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.entities.Course;
import org.generation.italy.legion.model.services.abstractions.AbstractDidacticService;
import org.generation.italy.legion.model.services.implementations.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/courses")
public class ApiCourseController {
    private AbstractDidacticService didacticService;
    private GenericService<Course> crudService;

    @Autowired
    public ApiCourseController(AbstractDidacticService didacticService,
                                GenericRepository<Course> courseRepo){
        this.didacticService = didacticService;
        this.crudService = new GenericService<>(courseRepo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> findById(@PathVariable long id){
        Optional<Course> c = crudService.findById(id);
        if(c.isPresent()){
            return ResponseEntity.ok().body(CourseDto.fromEntity(c.get()));
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id){
        Optional<Course> oCourse = crudService.findById(id);
        if(oCourse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        crudService.deleteById(id);
        return ResponseEntity.noContent().build();
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
                Iterable<Course> courseIterable = didacticService.findByTitleAndIsActive(part, active);
                return ResponseEntity.ok().body(CourseDto.fromEntityIterable(courseIterable));
            }else if(part!=null){
                Iterable<Course> courseIterable = didacticService.findByTitleAndIsActiveAndMinEdition(part, active, minEdition);
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

