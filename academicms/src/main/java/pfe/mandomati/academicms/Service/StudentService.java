package pfe.mandomati.academicms.Service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.academicms.Dto.StudentDto;
import pfe.mandomati.academicms.Dto.UserDto;

public interface StudentService {
    List<StudentDto> getAllStudents();
    StudentDto getStudentByStudentId(Long id);
    StudentDto createOrUpdateStudentProfile(StudentDto studentDto);
    void deleteStudentProfile(Long id);
    List<StudentDto> getStudentsByClassId(Long classId);
    List<StudentDto> getStudentsByCne(String cne);
    List<StudentDto> getStudentsByAdmissionDate(Date admissionDate);

    ResponseEntity<String> registerUser(UserDto userDTO);
    ResponseEntity<String> updateStudent(Long id, UserDto userDto);
    ResponseEntity<String> deleteStudent(Long id);
}
