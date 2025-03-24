package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Dto.ClassScheduleDto;
import pfe.mandomati.academicms.Model.ClassSchedule;
import pfe.mandomati.academicms.Model.TeacherAssignment;
import pfe.mandomati.academicms.Repository.ClassScheduleRepository;
import pfe.mandomati.academicms.Repository.TeacherAssignmentRepository;
import pfe.mandomati.academicms.Service.ClassScheduleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassScheduleServiceImpl implements ClassScheduleService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private TeacherAssignmentRepository teacherAssignmentRepository;

    @Override
    public ClassScheduleDto createClassSchedule(ClassScheduleDto classScheduleDto) {
        try {
            TeacherAssignment teacherAssignment = teacherAssignmentRepository.findById(classScheduleDto.getTeacherAssignementId())
                    .orElseThrow(() -> new RuntimeException("TeacherAssignment not found with id " + classScheduleDto.getTeacherAssignementId()));

            ClassSchedule classSchedule = ClassSchedule.builder()
                    .dayOfWeek(classScheduleDto.getDayOfWeek())
                    .startTime(classScheduleDto.getStartTime())
                    .endTime(classScheduleDto.getEndTime())
                    .roomNumber(classScheduleDto.getRoomNumber())
                    .teacher(teacherAssignment)
                    .build();

            classSchedule = classScheduleRepository.save(classSchedule);
            return entityToDto(classSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create class schedule: " + e.getMessage());
        }
    }

    @Override
    public ClassScheduleDto updateClassSchedule(Long id, ClassScheduleDto classScheduleDto) {
        try {
            ClassSchedule existingClassSchedule = classScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ClassSchedule not found with id " + id));

            TeacherAssignment teacherAssignment = teacherAssignmentRepository.findById(classScheduleDto.getTeacherAssignementId())
                    .orElseThrow(() -> new RuntimeException("TeacherAssignment not found with id " + classScheduleDto.getTeacherAssignementId()));

            existingClassSchedule.setDayOfWeek(classScheduleDto.getDayOfWeek());
            existingClassSchedule.setStartTime(classScheduleDto.getStartTime());
            existingClassSchedule.setEndTime(classScheduleDto.getEndTime());
            existingClassSchedule.setRoomNumber(classScheduleDto.getRoomNumber());
            existingClassSchedule.setTeacher(teacherAssignment);

            existingClassSchedule = classScheduleRepository.save(existingClassSchedule);
            return entityToDto(existingClassSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update class schedule: " + e.getMessage());
        }
    }

    @Override
    public void deleteClassSchedule(Long id) {
        try {
            ClassSchedule existingClassSchedule = classScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ClassSchedule not found with id " + id));
            classScheduleRepository.delete(existingClassSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete class schedule: " + e.getMessage());
        }
    }

    @Override
    public List<ClassScheduleDto> getAllClassSchedules() {
        try {
            List<ClassSchedule> classSchedules = classScheduleRepository.findAll();
            return classSchedules.stream().map(this::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all class schedules: " + e.getMessage());
        }
    }

    @Override
    public ClassScheduleDto getClassScheduleById(Long id) {
        try {
            ClassSchedule classSchedule = classScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ClassSchedule not found with id " + id));
            return entityToDto(classSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get class schedule by id: " + e.getMessage());
        }
    }

    private ClassScheduleDto entityToDto(ClassSchedule classSchedule) {
        return new ClassScheduleDto(
                classSchedule.getId(),
                classSchedule.getDayOfWeek(),
                classSchedule.getStartTime(),
                classSchedule.getEndTime(),
                classSchedule.getRoomNumber(),
                classSchedule.getTeacher().getId()
        );
    }
}