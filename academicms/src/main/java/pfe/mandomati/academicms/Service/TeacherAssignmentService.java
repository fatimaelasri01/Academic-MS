package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Model.TeacherAssignment;
import java.util.List;

public interface TeacherAssignmentService {
    TeacherAssignment saveTeacherAssignment(TeacherAssignment teacherAssignment);
    List<TeacherAssignment> getAllTeacherAssignments();
    TeacherAssignment getTeacherAssignmentById(Long id);
    TeacherAssignment updateTeacherAssignment(TeacherAssignment teacherAssignment);
    void deleteTeacherAssignment(Long id);
}