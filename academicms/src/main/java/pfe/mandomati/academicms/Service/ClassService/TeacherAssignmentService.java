package pfe.mandomati.academicms.Service.ClassService;

import pfe.mandomati.academicms.Dto.ClassDto.TeacherAssignmentDto;
import pfe.mandomati.academicms.Dto.ClassDto.TeacherDto;

import java.util.List;

public interface TeacherAssignmentService {
    TeacherAssignmentDto saveTeacherAssignment(TeacherAssignmentDto teacherAssignmentDto, String token);
    List<TeacherAssignmentDto> getAllTeacherAssignments();
    TeacherAssignmentDto getTeacherAssignmentById(Long id);
    TeacherAssignmentDto updateTeacherAssignment(Long id, TeacherAssignmentDto teacherAssignmentDto, String token);
    void deleteTeacherAssignment(Long id);
    List<TeacherDto> getAllTeachersRh();
}