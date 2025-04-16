package pfe.mandomati.academicms.Service.StudentService;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.academicms.Dto.StudentDto.StudentDto;
import pfe.mandomati.academicms.Dto.StudentDto.UserDto;

public interface StudentService {
    List<StudentDto> getAllStudents();
    StudentDto getStudentByStudentId(Long id);

    List<StudentDto> getStudentsByClassId(Long classId);
    StudentDto getStudentByCne(String cne);
    List<StudentDto> getStudentsByAdmissionDate(Date admissionDate);
    StudentDto getStudentByEmail(String email);
    List<StudentDto> getStudentByFullName(String firstName, String lastName);

    ResponseEntity<String> registerUser(UserDto userDTO);
    ResponseEntity<String> updateStudent(Long id, UserDto userDto);
    ResponseEntity<String> deleteStudent(Long id);

    StudentDto getStudentByToken(String token);
}
