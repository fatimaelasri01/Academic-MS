package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Dto.TeacherAssignmentDto;
import pfe.mandomati.academicms.Dto.TeacherDto;

import java.util.List;

public interface TeacherAssignmentService {
    TeacherAssignmentDto saveTeacherAssignment(TeacherAssignmentDto teacherAssignmentDto, String token);
    List<TeacherAssignmentDto> getAllTeacherAssignments();
    TeacherAssignmentDto getTeacherAssignmentById(Long id);
    TeacherAssignmentDto updateTeacherAssignment(Long id, TeacherAssignmentDto teacherAssignmentDto);
    void deleteTeacherAssignment(Long id);
    List<TeacherDto> getAllTeachersRh();
}