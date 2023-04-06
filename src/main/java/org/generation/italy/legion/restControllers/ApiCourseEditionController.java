package org.generation.italy.legion.restControllers;

import org.generation.italy.legion.dtos.SimpleCourseEditionDto;
import org.generation.italy.legion.model.entities.CourseEdition;
import org.generation.italy.legion.model.services.abstractions.AbstractCrudService;
import org.generation.italy.legion.model.services.abstractions.AbstractDidacticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/editions")
public class ApiCourseEditionController {

    private AbstractDidacticService didacticService;
    private AbstractCrudService<CourseEdition> editionService;
    @Autowired
    public ApiCourseEditionController(AbstractDidacticService didacticService, AbstractCrudService<CourseEdition> editionService){
        this.didacticService = didacticService;
        this.editionService = editionService;
    }
    @GetMapping("/{courseId}")
    public ResponseEntity<Iterable<SimpleCourseEditionDto>>findByCourseId(@PathVariable long courseId){
        Iterable<CourseEdition> iCe = didacticService.findCourseEditionsByCourse(courseId);
        return ResponseEntity.ok().body(SimpleCourseEditionDto.fromEntityIterable(iCe));
    }

    @GetMapping()
    public ResponseEntity<Iterable<SimpleCourseEditionDto>> findAllCourseEdition(@RequestParam(required = false) Boolean findMedian){
        Iterable<CourseEdition> iCe;
        if (findMedian != null && findMedian){
            iCe = didacticService.findCourseEditionMedianByCost();
        }else {
            iCe = didacticService.findAllCourseEdition();
        }
        return ResponseEntity.ok().body(SimpleCourseEditionDto.fromEntityIterable(iCe));
    }

    @GetMapping("/cost/mode")
    public ResponseEntity<Double> getCourseEditionCostMode(){
        Optional<Double> oCe = didacticService.findCourseEditionCostMode();
        if (oCe.isPresent()) {
            return ResponseEntity.ok().body(didacticService.findCourseEditionCostMode().get());
        }else {
            return ResponseEntity.notFound().build();
        }

    }

}
