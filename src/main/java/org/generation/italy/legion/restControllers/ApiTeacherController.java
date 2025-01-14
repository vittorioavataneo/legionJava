package org.generation.italy.legion.restControllers;

import org.generation.italy.legion.dtos.SimpleTeacherDto;
import org.generation.italy.legion.dtos.TeacherDto;
import org.generation.italy.legion.model.data.abstractions.GenericRepository;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.entities.Level;
import org.generation.italy.legion.model.entities.Teacher;
import org.generation.italy.legion.model.services.abstractions.AbstractDidacticService;
import org.generation.italy.legion.model.services.implementations.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/teachers") //davanti al prefisso dei metodi avremo "api"
public class ApiTeacherController {
    private AbstractDidacticService didacticService;
    private GenericService<Teacher> crudService;

    @Autowired
    public ApiTeacherController(AbstractDidacticService didacticService,
                                GenericRepository<Teacher> teacherRepo){
        this.didacticService = didacticService;
        this.crudService = new GenericService<>(teacherRepo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> findById(@PathVariable long id){
        Optional<Teacher> teacherOp = crudService.findById(id);
        if(teacherOp.isPresent()){
            return ResponseEntity.ok().body(TeacherDto.fromEntity(teacherOp.get()));
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping()
    public ResponseEntity<Iterable<SimpleTeacherDto>> findWithSkillAndLevel(@RequestParam(required = false) Long skillId,
                                                                  @RequestParam(required = false) Level level){
        try {
            Iterable<Teacher> teacherIt = didacticService.findWithSkillAndLevel(skillId, level);
            return ResponseEntity.ok().body(SimpleTeacherDto.fromEntityIterable(teacherIt, skillId));

        } catch (DataException e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
