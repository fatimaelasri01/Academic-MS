package pfe.mandomati.academicms.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pfe.mandomati.academicms.Dto.AttendanceDto;
import pfe.mandomati.academicms.Dto.SubjectDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Attendance;
import pfe.mandomati.academicms.Model.StudentAcademicProfile;
import pfe.mandomati.academicms.Model.Subject;
import pfe.mandomati.academicms.Repository.AttendanceRepository;
import pfe.mandomati.academicms.Repository.StudentAcademicProfileRepository;
import pfe.mandomati.academicms.Service.AttendanceService;

import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService{

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private SubjectServiceImpl subjectService;

    @Autowired
    private StudentAcademicProfileRepository studentAcademicProfileRepository;

    @Override
    public AttendanceDto createAttendance(AttendanceDto attendanceDto) {    
        Attendance attendance = new Attendance();

        attendance.setDate(attendanceDto.getDate());
        attendance.setStatus(attendanceDto.getStatus());
        attendance.setJustificationType(attendanceDto.getJustificationType());
        attendance.setJustificationDoc(attendanceDto.getJustificationDoc());
        attendance.setValidatorName(attendanceDto.getValidatorName());

        SubjectDto subjectDto = subjectService.getSubjectById(attendanceDto.getSubjectId());
        Subject subject = subjectService.dtoToSubject(subjectDto);

        StudentAcademicProfile student = studentAcademicProfileRepository.findById(attendanceDto.getStudentId()).get();

        attendance.setSubject(subject);
        attendance.setStudentProfile(student);

        attendanceRepository.save(attendance);
    
        return attendanceToDto(attendance);
    }

    @Override
    public AttendanceDto updateAttendance(Long id, AttendanceDto attendanceDto) {
        Attendance attendance = attendanceRepository.findById(id).
            orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id " + id));

        attendance.setDate(attendanceDto.getDate());
        attendance.setStatus(attendanceDto.getStatus());
        attendance.setJustificationType(attendanceDto.getJustificationType());
        attendance.setJustificationDoc(attendanceDto.getJustificationDoc());
        attendance.setValidatorName(attendanceDto.getValidatorName());

        attendanceRepository.save(attendance);

        return attendanceToDto(attendance);
    }

    @Override
    public void deleteAttendance(Long id) {
        Attendance attendance = attendanceRepository.findById(id).
            orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id " + id));

        attendanceRepository.delete(attendance);    
    }

    @Override
    public List<AttendanceDto> getAllAttendances() {
        List<Attendance> attendances = attendanceRepository.findAll();
        return attendances.stream().map(this::attendanceToDto).collect(Collectors.toList());
    }

    @Override
    public AttendanceDto getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id).
            orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id " + id));
        return attendanceToDto(attendance);
    }

    @Override
    public List<AttendanceDto> getAttendancesByStudentId(Long studentId) {
        StudentAcademicProfile student = studentAcademicProfileRepository.findById(studentId).
            orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + studentId));
        Optional<Attendance> attendances = attendanceRepository.findByStudentProfile(student);
        if (attendances.isEmpty()) {
            throw new ResourceNotFoundException("Attendances not found for student with id " + studentId);
        }
        return attendances.stream().map(this::attendanceToDto).collect(Collectors.toList());
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
            attendance.getSubject().getSubjectId()
        );
    }    
}
