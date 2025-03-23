package pfe.mandomati.academicms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pfe.mandomati.academicms.Dto.SubjectDto;
import pfe.mandomati.academicms.Service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createSubject(@RequestBody SubjectDto subjectDto) {
        SubjectDto createdSubject = subjectService.createSubject(subjectDto);
        return ResponseEntity.ok("Subject successfully created with ID: " + createdSubject.getSubjectId());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateSubject(@PathVariable Long id, @RequestBody SubjectDto subjectDto) {
        SubjectDto updatedSubject = subjectService.updateSubject(id, subjectDto);
        return ResponseEntity.ok("Subject successfully updated with ID: " + updatedSubject.getSubjectId());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok("Subject successfully deleted with ID: " + id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<SubjectDto>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<SubjectDto> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @PostMapping("/{subjectId}/assign/{classId}")
    public ResponseEntity<Void> assignSubjectToClass(@PathVariable Long subjectId, @PathVariable Long classId) {
        subjectService.assignSubjectToClass(subjectId, classId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{subjectId}/remove/{classId}")
    public ResponseEntity<Void> removeSubjectFromClass(@PathVariable Long subjectId, @PathVariable Long classId) {
        subjectService.removeSubjectFromClass(subjectId, classId);
        return ResponseEntity.ok().build();
    }
}