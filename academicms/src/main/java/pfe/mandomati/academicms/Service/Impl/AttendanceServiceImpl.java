package pfe.mandomati.academicms.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pfe.mandomati.academicms.Dto.AttendanceDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Attendance;
import pfe.mandomati.academicms.Model.ClassSchedule;
import pfe.mandomati.academicms.Model.Student;
import pfe.mandomati.academicms.Repository.AttendanceRepository;
import pfe.mandomati.academicms.Repository.ClassScheduleRepository;
import pfe.mandomati.academicms.Repository.StudentRepository;
import pfe.mandomati.academicms.Service.AttendanceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private StudentRepository studentAcademicProfileRepository;

    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    @Override
    public AttendanceDto createAttendance(AttendanceDto attendanceDto) {
        try {
            Attendance attendance = new Attendance();

            attendance.setDate(attendanceDto.getDate());
            attendance.setStatus(attendanceDto.getStatus());
            attendance.setJustificationType(attendanceDto.getJustificationType());
            attendance.setJustificationDoc(attendanceDto.getJustificationDoc());
            attendance.setValidatorName(attendanceDto.getValidatorName());

            ClassSchedule classSchedule = classScheduleRepository.findById(attendanceDto.getClassScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("ClassSchedule not found with id " + attendanceDto.getClassScheduleId()));

            Student student = studentAcademicProfileRepository.findById(attendanceDto.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + attendanceDto.getStudentId()));

            attendance.setClassSchedule(classSchedule);
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

            attendance.setDate(attendanceDto.getDate());
            attendance.setStatus(attendanceDto.getStatus());
            attendance.setJustificationType(attendanceDto.getJustificationType());
            attendance.setJustificationDoc(attendanceDto.getJustificationDoc());
            attendance.setValidatorName(attendanceDto.getValidatorName());

            ClassSchedule classSchedule = classScheduleRepository.findById(attendanceDto.getClassScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("ClassSchedule not found with id " + attendanceDto.getClassScheduleId()));

            Student student = studentAcademicProfileRepository.findById(attendanceDto.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + attendanceDto.getStudentId()));

            attendance.setClassSchedule(classSchedule);
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
                attendance.getDate(),
                attendance.getStatus(),
                attendance.getJustificationType(),
                attendance.getJustificationDoc(),
                attendance.getValidatorName(),
                attendance.getClassSchedule().getId()
        );
    }
}