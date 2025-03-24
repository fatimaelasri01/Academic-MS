package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Dto.ClassScheduleDto;
import java.util.List;

public interface ClassScheduleService {
    ClassScheduleDto createClassSchedule(ClassScheduleDto classScheduleDto);
    ClassScheduleDto updateClassSchedule(Long id, ClassScheduleDto classScheduleDto);
    void deleteClassSchedule(Long id);
    List<ClassScheduleDto> getAllClassSchedules();
    ClassScheduleDto getClassScheduleById(Long id);
}