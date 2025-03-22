package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Dto.AttendanceDto;
import java.util.List;

public interface AttendanceService {
    AttendanceDto createAttendance(AttendanceDto attendanceDto);
    AttendanceDto updateAttendance(Long id, AttendanceDto attendanceDto);
    void deleteAttendance(Long id);
    List<AttendanceDto> getAllAttendances();
    AttendanceDto getAttendanceById(Long id);
    List<AttendanceDto> getAttendancesByStudentId(Long studentId); // Nouvelle méthode
}