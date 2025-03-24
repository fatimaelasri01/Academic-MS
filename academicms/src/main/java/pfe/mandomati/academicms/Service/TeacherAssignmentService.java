package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Dto.TeacherAssignmentDto;
import java.util.List;

public interface TeacherAssignmentService {
    TeacherAssignmentDto saveTeacherAssignment(TeacherAssignmentDto teacherAssignmentDto);
    List<TeacherAssignmentDto> getAllTeacherAssignments();
    TeacherAssignmentDto getTeacherAssignmentById(Long id);
    TeacherAssignmentDto updateTeacherAssignment(Long id, TeacherAssignmentDto teacherAssignmentDto);
    void deleteTeacherAssignment(Long id);
}