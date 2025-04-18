package pfe.mandomati.academicms.Controller.ClassController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pfe.mandomati.academicms.Dto.ClassDto.SubjectDto;
import pfe.mandomati.academicms.Service.ClassService.SubjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {

    private final SubjectService subjectService;

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

    @PostMapping("/{subjectId}/assign/{filiereId}")
    public ResponseEntity<String> assignSubjectToFiliere(@PathVariable Long subjectId, @PathVariable Long filiereId) {
        subjectService.assignSubjectToFiliere(subjectId, filiereId);
        return ResponseEntity.ok("Subject successfully assigned to filiere with ID: " + filiereId);
    }

    @DeleteMapping("/{subjectId}/remove/{filiereId}")
    public ResponseEntity<String> removeSubjectFromFiliere(@PathVariable Long subjectId, @PathVariable Long filiereId) {
        subjectService.removeSubjectFromFiliere(subjectId, filiereId);
        return ResponseEntity.ok("Subject successfully removed from filiere with ID: " + filiereId);
    }
}