package pfe.mandomati.academicms.Service.Impl.ClassImpl;

import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Client.RhClient;
import pfe.mandomati.academicms.Dto.ClassDto.TeacherAssignmentDto;
import pfe.mandomati.academicms.Dto.ClassDto.TeacherDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Class.Class;
import pfe.mandomati.academicms.Model.Class.Subject;
import pfe.mandomati.academicms.Model.Class.TeacherAssignment;
import pfe.mandomati.academicms.Repository.ClassRepo.ClassRepository;
import pfe.mandomati.academicms.Repository.ClassRepo.SubjectRepository;
import pfe.mandomati.academicms.Repository.ClassRepo.TeacherAssignmentRepository;
import pfe.mandomati.academicms.Service.ClassService.TeacherAssignmentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    private TeacherAssignmentRepository teacherAssignmentRepository;

    private ClassRepository classRepository;

    private SubjectRepository subjectRepository;

    private RhClient rhClient;

    @Override
    public TeacherAssignmentDto saveTeacherAssignment(TeacherAssignmentDto teacherAssignmentDto, String token) {
        try {
            // Vérifier si l'enseignant existe
            ResponseEntity<?> teacherResponse = rhClient.getTeacherById(teacherAssignmentDto.getTeacherId(), token);
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
    public TeacherAssignmentDto updateTeacherAssignment(Long id, TeacherAssignmentDto teacherAssignmentDto, String token) {
        try {
            TeacherAssignment existingAssignment = teacherAssignmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("TeacherAssignment not found with id " + id));

            // Vérifier si l'enseignant existe
            ResponseEntity<?> teacherResponse = rhClient.getTeacherById(teacherAssignmentDto.getTeacherId(), token);
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