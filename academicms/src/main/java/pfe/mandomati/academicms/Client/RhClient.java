package pfe.mandomati.academicms.Client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import pfe.mandomati.academicms.Dto.TeacherDto;

@FeignClient(name = "rhms", url = "https://rhms.mandomati.com")
public interface RhClient {

    @GetMapping("/api/teacher/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/api/teacher/speciality/{speciality}")
    public ResponseEntity<?> getTeachersBySpeciality(@PathVariable String speciality);
    
    @GetMapping("/api/teacher/allteachers")
    public List<TeacherDto> getAllTeachersDs();
    
}
