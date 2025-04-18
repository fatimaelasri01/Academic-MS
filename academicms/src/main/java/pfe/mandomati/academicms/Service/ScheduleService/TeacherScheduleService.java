package pfe.mandomati.academicms.Service.ScheduleService;


import org.springframework.web.multipart.MultipartFile;
import pfe.mandomati.academicms.Dto.ScheduleDto.TeacherScheduleDto;
import pfe.mandomati.academicms.Model.Schedule.TeacherSchedule;

import java.util.List;
import java.util.Optional;

public interface TeacherScheduleService {
    TeacherScheduleDto createTeacherSchedule(TeacherScheduleDto teacherScheduleDto, MultipartFile file);
    TeacherScheduleDto updateTeacherSchedule(Long id, TeacherScheduleDto teacherScheduleDto, MultipartFile file);
    void deleteTeacherSchedule(Long id);
    List<TeacherScheduleDto> getAllTeacherSchedules();
    TeacherScheduleDto getTeacherScheduleById(Long id);
    TeacherScheduleDto findByTeacher(String token);
}
