package pfe.mandomati.academicms.Controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pfe.mandomati.academicms.Dto.StudentDto;
import pfe.mandomati.academicms.Dto.UserDto;
import pfe.mandomati.academicms.Service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDto> getStudentProfile(@RequestHeader("Authorisation") String authorizationHeader ) {
        String token = authorizationHeader.replace("Bearer ", "");
        StudentDto student = studentService.getStudentByToken(token);
        return ResponseEntity.ok(student);        
    }

    // create student
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("create")
    public ResponseEntity<String> createStudent(@RequestBody UserDto userDto) {
        return studentService.registerUser(userDto);
    }

    // update student
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody UserDto userDto) {
        return studentService.updateStudent(id, userDto);
    }

    // delete student
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    // get all students
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @GetMapping("all")
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    // get student by id
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable Long id) {
        return studentService.getStudentByStudentId(id);
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'TEACHER')")
    public ResponseEntity<List<StudentDto>> getStudentsByClassId(@PathVariable Long classId) {
        List<StudentDto> students = studentService.getStudentsByClassId(classId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/cne/{cne}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'TEACHER')")
    public ResponseEntity<StudentDto> getStudentByCne(@PathVariable String cne) {
        StudentDto student = studentService.getStudentByCne(cne);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/admission-date/{admissionDate}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<StudentDto>> getStudentsByAdmissionDate(@PathVariable Date admissionDate) {
        List<StudentDto> students = studentService.getStudentsByAdmissionDate(admissionDate);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'TEACHER')")
    public ResponseEntity<StudentDto> getStudentByEmail(@PathVariable String email) {
        StudentDto student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/fullname")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'TEACHER')")
    public ResponseEntity<List<StudentDto>> getStudentByFullName(@RequestParam String firstName, @RequestParam String lastName) {
        List<StudentDto> students = studentService.getStudentByFullName(firstName, lastName);
        return ResponseEntity.ok(students);
    }
}
