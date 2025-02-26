package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Model.TeacherAssignment;
import pfe.mandomati.academicms.Repository.TeacherAssignmentRepository;
import pfe.mandomati.academicms.Service.TeacherAssignmentService;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    @Autowired
    private TeacherAssignmentRepository teacherAssignmentRepository;

    @Override
    public TeacherAssignment saveTeacherAssignment(TeacherAssignment teacherAssignment) {
        return teacherAssignmentRepository.save(teacherAssignment);
    }

    @Override
    public List<TeacherAssignment> getAllTeacherAssignments() {
        return teacherAssignmentRepository.findAll();
    }

    @Override
    public TeacherAssignment getTeacherAssignmentById(Long id) {
        Optional<TeacherAssignment> optionalTeacherAssignment = teacherAssignmentRepository.findById(id);
        return optionalTeacherAssignment.orElse(null);
    }

    @Override
    public TeacherAssignment updateTeacherAssignment(TeacherAssignment teacherAssignment) {
        return teacherAssignmentRepository.save(teacherAssignment);
    }

    @Override
    public void deleteTeacherAssignment(Long id) {
        teacherAssignmentRepository.deleteById(id);
    }
}