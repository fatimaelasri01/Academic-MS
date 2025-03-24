package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Client.RhClient;
import pfe.mandomati.academicms.Dto.TeacherAssignmentDto;
import pfe.mandomati.academicms.Dto.TeacherDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Class;
import pfe.mandomati.academicms.Model.Subject;
import pfe.mandomati.academicms.Model.TeacherAssignment;
import pfe.mandomati.academicms.Repository.ClassRepository;
import pfe.mandomati.academicms.Repository.SubjectRepository;
import pfe.mandomati.academicms.Repository.TeacherAssignmentRepository;
import pfe.mandomati.academicms.Service.TeacherAssignmentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    @Autowired
    private TeacherAssignmentRepository teacherAssignmentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private RhClient rhClient;

    @Override
    public TeacherAssignmentDto saveTeacherAssignment(TeacherAssignmentDto teacherAssignmentDto) {
        try {
            // Vérifier si l'enseignant existe
            ResponseEntity<?> teacherResponse = rhClient.getTeacherById(teacherAssignmentDto.getTeacherId());
            if (!teacherResponse.getStatusCode().is2xxSuccessful()) {
                throw new ResourceNotFoundException("Teacher not found with id " + teacherAssignmentDto.getTeacherId());
            }

            // Vérifier si la classe existe
            Class schoolClass = classRepository.findById(teacherAssignmentDto.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found with id " + teacherAssignmentDto.getClassId()));

            // Vérifier si le sujet existe
            Subject subject = subjectRepository.findById(teacherAssignmentDto.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + teacherAssignmentDto.getSubjectId()));

            TeacherAssignment teacherAssignment = TeacherAssignment.builder()
                    .teacherId(teacherAssignmentDto.getTeacherId())
                    .academicYear(teacherAssignmentDto.getAcademicYear())
                    .hoursPerWeek(teacherAssignmentDto.getHoursPerWeek())
                    .schoolClass(schoolClass)
                    .subject(subject)
                    .build();

            teacherAssignment = teacherAssignmentRepository.save(teacherAssignment);
            return entityToDto(teacherAssignment);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("Failed to save teacher assignment: " + e.getMessage());
        }
    }

    @Override
    public List<TeacherAssignmentDto> getAllTeacherAssignments() {
        List<TeacherAssignment> teacherAssignments = teacherAssignmentRepository.findAll();
        return teacherAssignments.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public TeacherAssignmentDto getTeacherAssignmentById(Long id) {
        TeacherAssignment teacherAssignment = teacherAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TeacherAssignment not found with id " + id));
        return entityToDto(teacherAssignment);
    }

    @Override
    public TeacherAssignmentDto updateTeacherAssignment(Long id, TeacherAssignmentDto teacherAssignmentDto) {
        try {
            TeacherAssignment existingAssignment = teacherAssignmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("TeacherAssignment not found with id " + id));

            // Vérifier si l'enseignant existe
            ResponseEntity<?> teacherResponse = rhClient.getTeacherById(teacherAssignmentDto.getTeacherId());
            if (!teacherResponse.getStatusCode().is2xxSuccessful()) {
                throw new ResourceNotFoundException("Teacher not found with id " + teacherAssignmentDto.getTeacherId());
            }

            // Vérifier si la classe existe
            Class schoolClass = classRepository.findById(teacherAssignmentDto.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found with id " + teacherAssignmentDto.getClassId()));

            // Vérifier si le sujet existe
            Subject subject = subjectRepository.findById(teacherAssignmentDto.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + teacherAssignmentDto.getSubjectId()));

            existingAssignment.setTeacherId(teacherAssignmentDto.getTeacherId());
            existingAssignment.setAcademicYear(teacherAssignmentDto.getAcademicYear());
            existingAssignment.setHoursPerWeek(teacherAssignmentDto.getHoursPerWeek());
            existingAssignment.setSchoolClass(schoolClass);
            existingAssignment.setSubject(subject);

            existingAssignment = teacherAssignmentRepository.save(existingAssignment);
            return entityToDto(existingAssignment);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("Failed to update teacher assignment: " + e.getMessage());
        }
    }

    @Override
    public void deleteTeacherAssignment(Long id) {
        try {
            teacherAssignmentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete teacher assignment: " + e.getMessage());
        }
    }

    @Override
    public List<TeacherDto> getAllTeachersRh() {
        List<TeacherDto> teachersResponse = rhClient.getAllTeachersDs();
        if (teachersResponse == null) {
            throw new ResourceNotFoundException("Failed to fetch teachers from RHMS");
        }
        return teachersResponse;   
    }

    private TeacherAssignmentDto entityToDto(TeacherAssignment teacherAssignment) {
        return new TeacherAssignmentDto(
                teacherAssignment.getId(),
                teacherAssignment.getTeacherId(),
                teacherAssignment.getAcademicYear(),
                teacherAssignment.getHoursPerWeek(),
                teacherAssignment.getSchoolClass().getId(),
                teacherAssignment.getSubject().getSubjectId()
        );
    }
}