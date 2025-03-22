package pfe.mandomati.academicms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pfe.mandomati.academicms.Dto.ClassDto;
import pfe.mandomati.academicms.Exception.ClassCreationException;
import pfe.mandomati.academicms.Service.ClassService;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addClass(@RequestBody ClassDto classDto) {
        try {
            ClassDto newClass = classService.addClass(classDto);
            return ResponseEntity.ok("Class successfully created with id : " + newClass.getClassId());
        } catch (ClassCreationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateClass(@PathVariable Long id, @RequestBody ClassDto classDto) {
        try {
            ClassDto updatedClass = classService.updateClass(id, classDto);
            return ResponseEntity.ok("Class successfully updated with id : " + updatedClass.getClassId());
        } catch (ClassCreationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteClass(@PathVariable Long id) {
        try {
            classService.deleteClass(id);
            return ResponseEntity.ok("class successfully deleted with ID: " + id);
        } catch (ClassCreationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<ClassDto>> getAllClasses() {
        List<ClassDto> classes = classService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<ClassDto> getClassById(@PathVariable Long id) {
        try {
            ClassDto classDto = classService.getClassById(id);
            return ResponseEntity.ok(classDto);
        } catch (ClassCreationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<ClassDto> getClassByName(@PathVariable String name) {
        try {
            ClassDto classDto = classService.getClassByName(name);
            return ResponseEntity.ok(classDto);
        } catch (ClassCreationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}