package pfe.mandomati.academicms.Controller.ClassController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pfe.mandomati.academicms.Dto.ClassDto.TeacherAssignmentDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Service.ClassService.TeacherAssignmentService;

import pfe.mandomati.academicms.Dto.ClassDto.TeacherDto;

import java.util.List;

@RestController
@RequestMapping("/teacher-assignment")
public class TeacherAssignmentController {

    @Autowired
    private TeacherAssignmentService teacherAssignmentService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createTeacherAssignment(@RequestBody TeacherAssignmentDto teacherAssignmentDto, @RequestHeader("Authorization") String token) {
        try {
            TeacherAssignmentDto createdAssignment = teacherAssignmentService.saveTeacherAssignment(teacherAssignmentDto, token);
            return new ResponseEntity<>("Teacher assignment successfully created with ID: " + createdAssignment.getId(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to create teacher assignment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<TeacherAssignmentDto>> getAllTeacherAssignments() {
        List<TeacherAssignmentDto> assignments = teacherAssignmentService.getAllTeacherAssignments();
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<?> getTeacherAssignmentById(@PathVariable Long id) {
        try {
            TeacherAssignmentDto assignment = teacherAssignmentService.getTeacherAssignmentById(id);
            return new ResponseEntity<>(assignment, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("Teacher assignment not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateTeacherAssignment(@PathVariable Long id, @RequestBody TeacherAssignmentDto teacherAssignmentDto, @RequestHeader("Authorization") String token) {
        try {
            TeacherAssignmentDto updatedAssignment = teacherAssignmentService.updateTeacherAssignment(id, teacherAssignmentDto, token);
            return new ResponseEntity<>("Teacher assignment successfully updated with ID: " + updatedAssignment.getId(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to update teacher assignment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTeacherAssignment(@PathVariable Long id) {
        try {
            teacherAssignmentService.deleteTeacherAssignment(id);
            return new ResponseEntity<>("Teacher assignment successfully deleted with ID: " + id, HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to delete teacher assignment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<String> getAllTeachersRh() {
        List<TeacherDto> teachers = teacherAssignmentService.getAllTeachersRh();
        return new ResponseEntity<>(teachers.toString(), HttpStatus.OK);
    }
}