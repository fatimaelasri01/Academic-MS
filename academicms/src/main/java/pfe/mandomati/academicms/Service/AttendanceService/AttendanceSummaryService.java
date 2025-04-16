package pfe.mandomati.academicms.Service.AttendanceService;

import  pfe.mandomati.academicms.Dto.AttendanceDto.AttendanceSummaryDto;
import java.util.List;

public interface AttendanceSummaryService {
    List<AttendanceSummaryDto> getSummaryByStudent(Long studentId);
    List<AttendanceSummaryDto> getSummaryByClass(Long classId);
    List<AttendanceSummaryDto> getTop3ClassesWithMostAbsences();
    AttendanceSummaryDto getGlobalSummaryByAcademicYear(String academicYear);
    List<AttendanceSummaryDto> getTopAbsentStudents();
    List<AttendanceSummaryDto> getSummaryBySubject(Long subjectId);
    List<AttendanceSummaryDto> getSummaryByTeacher(Long teacherId);
}