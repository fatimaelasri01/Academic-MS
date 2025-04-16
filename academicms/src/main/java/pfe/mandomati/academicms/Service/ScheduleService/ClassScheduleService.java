package pfe.mandomati.academicms.Service.ScheduleService;

import pfe.mandomati.academicms.Dto.ScheduleDto.ClassScheduleDto;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ClassScheduleService {
    ClassScheduleDto updateClassSchedule(Long id, ClassScheduleDto classScheduleDto, MultipartFile file);
    ClassScheduleDto createClassSchedule(ClassScheduleDto classScheduleDto, MultipartFile file);
    void deleteClassSchedule(Long id);
    List<ClassScheduleDto> getAllClassSchedules();
    ClassScheduleDto getClassScheduleById(Long id);
}