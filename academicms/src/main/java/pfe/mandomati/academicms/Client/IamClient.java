package pfe.mandomati.academicms.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pfe.mandomati.academicms.Dto.IamDto;
import pfe.mandomati.academicms.Dto.IamTeacherDto;

import java.util.List;


@FeignClient(name = "iamms", url = "https://iamms.mandomati.com")
public interface IamClient {

    @PostMapping("/api/auth/register")
    ResponseEntity<String> registerUser(@RequestBody IamDto userDto);

    @GetMapping("/api/auth/user/{email}")
    ResponseEntity<String> checkUserExists(@PathVariable String email);

    @GetMapping("/api/auth/user/role/student/{id}")
    ResponseEntity<IamDto> getStudentById(@PathVariable("id") String id);
    
    @GetMapping("/api/auth/user/role/student")
    ResponseEntity<List<IamDto>> getAllStudents();
    
    @GetMapping("/api/auth/user/role/teacher/{id}")
    ResponseEntity<IamTeacherDto> getTeacherById(@PathVariable("id") String id);
    
    @GetMapping("/api/auth/user/role/teacher")
    ResponseEntity<List<IamTeacherDto>> getAllTeachers();

    @GetMapping("/api/auth/user/role/parent/{id}")
    ResponseEntity<IamDto> getParentById(@PathVariable("id") String id);
    
    @GetMapping("/api/auth/user/role/parent")
    ResponseEntity<List<IamDto>> getAllParents();

    @PutMapping("/api/auth/user/edit/{username}")
    ResponseEntity<String> editUser(@PathVariable String username, @RequestBody IamDto userDto);

    @DeleteMapping("/api/auth/user/delete/{username}")
    ResponseEntity<String> deleteUser(@PathVariable String username);

}
