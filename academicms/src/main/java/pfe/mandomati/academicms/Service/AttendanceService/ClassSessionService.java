package pfe.mandomati.academicms.Service.AttendanceService;

import pfe.mandomati.academicms.Dto.AttendanceDto.ClassSessionDto;

import java.util.List;

public interface ClassSessionService {
    ClassSessionDto createClassSession(ClassSessionDto classSessionDto);
    ClassSessionDto updateClassSession(Long id, ClassSessionDto classSessionDto);
    void deleteClassSession(Long id);
    ClassSessionDto getClassSessionById(Long id);
    List<ClassSessionDto> getAllClassSessions();
}