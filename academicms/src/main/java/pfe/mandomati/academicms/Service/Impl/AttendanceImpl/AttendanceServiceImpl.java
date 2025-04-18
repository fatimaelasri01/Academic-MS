package pfe.mandomati.academicms.Service.Impl.AttendanceImpl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pfe.mandomati.academicms.Dto.AttendanceDto.AttendanceDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Attendance.Attendance;
import pfe.mandomati.academicms.Model.Attendance.ClassSession;
import pfe.mandomati.academicms.Model.Student.Student;
import pfe.mandomati.academicms.Repository.AttendanceRepo.AttendanceRepository;
import pfe.mandomati.academicms.Repository.AttendanceRepo.ClassSessionRepository;
import pfe.mandomati.academicms.Repository.StudentRepo.StudentRepository;
import pfe.mandomati.academicms.Service.AttendanceService.AttendanceService;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final ClassSessionRepository classSessionRepository;

    private final StudentRepository studentAcademicProfileRepository;

    @Override
    public AttendanceDto createAttendance(AttendanceDto attendanceDto) {
        try {
            Attendance attendance = new Attendance();

            attendance.setStatus(attendanceDto.getStatus());
            attendance.isExcused();
            attendance.setJustificationType(null);
            attendance.setJustificationDoc(null);
            
            ClassSession classSession = classSessionRepository.findById(attendanceDto.getClassSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession not found with id " + attendanceDto.getClassSessionId()));

            attendance.setClassSession(classSession);
            Student student = studentAcademicProfileRepository.findById(attendanceDto.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + attendanceDto.getStudentId()));

            
            attendance.setStudentProfile(student);

            attendanceRepository.save(attendance);

            return attendanceToDto(attendance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create attendance: " + e.getMessage());
        }
    }

    @Override
    public AttendanceDto updateAttendance(Long id, AttendanceDto attendanceDto) {
        try {
            Attendance attendance = attendanceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id " + id));

            attendance.setStatus(attendanceDto.getStatus());
            attendance.setExcused(attendanceDto.isExcused());
            attendance.setJustificationType(attendanceDto.getJustificationType());
            attendance.setJustificationDoc(attendanceDto.getJustificationDoc());

            ClassSession classSession = classSessionRepository.findById(attendanceDto.getClassSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession not found with id " + attendanceDto.getClassSessionId()));
            attendance.setClassSession(classSession);

            Student student = studentAcademicProfileRepository.findById(attendanceDto.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + attendanceDto.getStudentId()));

            attendance.setStudentProfile(student);

            attendanceRepository.save(attendance);

            return attendanceToDto(attendance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update attendance: " + e.getMessage());
        }
    }

    @Override
    public void deleteAttendance(Long id) {
        try {
            Attendance attendance = attendanceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id " + id));
            attendanceRepository.delete(attendance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete attendance: " + e.getMessage());
        }
    }

    @Override
    public List<AttendanceDto> getAllAttendances() {
        try {
            List<Attendance> attendances = attendanceRepository.findAll();
            return attendances.stream().map(this::attendanceToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all attendances: " + e.getMessage());
        }
    }

    @Override
    public AttendanceDto getAttendanceById(Long id) {
        try {
            Attendance attendance = attendanceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id " + id));
            return attendanceToDto(attendance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get attendance by id: " + e.getMessage());
        }
    }

    @Override
    public List<AttendanceDto> getAttendancesByStudentId(Long studentId) {
        try {
            Student student = studentAcademicProfileRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + studentId));

            List<Attendance> attendances = attendanceRepository.findByStudentProfile(student);

            if (attendances == null || attendances.isEmpty()) {
                throw new ResourceNotFoundException("Attendances not found for student with id " + studentId);
            }

            return attendances.stream().map(this::attendanceToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get attendances by student id: " + e.getMessage());
        }
    }

    private AttendanceDto attendanceToDto(Attendance attendance) {
        return new AttendanceDto(
                attendance.getId(),
                attendance.getStudentProfile().getStudentId(),
                attendance.getStatus(),
                attendance.isExcused(),
                attendance.getJustificationType(),
                attendance.getJustificationDoc(),
                attendance.getClassSession().getId()
        );
    }
}