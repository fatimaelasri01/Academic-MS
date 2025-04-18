package pfe.mandomati.academicms.Controller.ClassController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pfe.mandomati.academicms.Dto.ClassDto.ClassDto;
import pfe.mandomati.academicms.Service.ClassService.ClassService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/class")
public class ClassController {

    private final ClassService classService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassDto> createClass(@RequestBody ClassDto classDto) {
        ClassDto createdClass = classService.addClass(classDto);
        return new ResponseEntity<>(createdClass, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassDto> updateClass(@PathVariable Long id, @RequestBody ClassDto classDto) {
        ClassDto updatedClass = classService.updateClass(id, classDto);
        return new ResponseEntity<>(updatedClass, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<ClassDto>> getAllClasses() {
        List<ClassDto> classes = classService.getAllClasses();
        return new ResponseEntity<>(classes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<ClassDto> getClassById(@PathVariable Long id) {
        ClassDto classDto = classService.getClassById(id);
        return new ResponseEntity<>(classDto, HttpStatus.OK);
    }

    @GetMapping("/filiere/{filiereName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<ClassDto>> getClassesByFiliere(@PathVariable String filiereName) {
        List<ClassDto> classDtos = classService.getClassesByFiliere(filiereName);
        return new ResponseEntity<>(classDtos, HttpStatus.OK);
    }

    @GetMapping("/name/{filiereName}/{numero}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<ClassDto> getClassByName(@PathVariable String filiereName, @PathVariable int numero) {
        ClassDto classDto = classService.getClassByName(filiereName, numero);
        return new ResponseEntity<>(classDto, HttpStatus.OK);
    }
}