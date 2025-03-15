package pfe.mandomati.academicms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pfe.mandomati.academicms.Dto.UserDto;
import pfe.mandomati.academicms.Service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



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

    @GetMapping("test")
    public String test() {
        return "test";
    }
    
    

    
}
