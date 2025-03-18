package pfe.mandomati.academicms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pfe.mandomati.academicms.Dto.StudentDto;
import pfe.mandomati.academicms.Dto.UserDto;
import pfe.mandomati.academicms.Service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'RH')")
    @PostMapping("create")
    public ResponseEntity<String> createStudent(@RequestBody UserDto userDto) {
        return studentService.registerUser(userDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'RH')")
    @GetMapping("all")
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'RH')")
    @GetMapping("student/{id}")
    public StudentDto getStudentById(Long id) {
        return studentService.getStudentByStudentId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'RH')")
    @PutMapping("update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody UserDto userDto) {
        return studentService.updateStudent(id, userDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'RH')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }


    @GetMapping("test")
    public String test() {
        return "test";
    }
    
    

    
}
