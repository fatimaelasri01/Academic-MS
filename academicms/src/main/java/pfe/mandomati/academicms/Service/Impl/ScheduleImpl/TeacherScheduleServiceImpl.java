package pfe.mandomati.academicms.Service.Impl.ScheduleImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pfe.mandomati.academicms.Client.IamClient;
import pfe.mandomati.academicms.Dto.ScheduleDto.TeacherScheduleDto;
import pfe.mandomati.academicms.Dto.StudentDto.IamDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Schedule.TeacherSchedule;
import pfe.mandomati.academicms.Repository.ScheduleRepo.TeacherScheduleRepository;
import pfe.mandomati.academicms.Service.Impl.Utils.ExtractUsernameFromToken;
import pfe.mandomati.academicms.Service.Impl.Utils.FileUtil;
import pfe.mandomati.academicms.Service.ScheduleService.TeacherScheduleService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeacherScheduleServiceImpl implements TeacherScheduleService {

    private final TeacherScheduleRepository teacherScheduleRepository;
    private final IamClient iamClient;

    @Value("${file.upload.schedule-directory}")
    private String uploadDir;

    @Override
    public TeacherScheduleDto createTeacherSchedule(TeacherScheduleDto teacherScheduleDto, MultipartFile file) {
        try {
            String filePath = FileUtil.saveFile(uploadDir, file, "teacher-schedule");

            TeacherSchedule teacherSchedule = TeacherSchedule.builder()
                    .teacherId(teacherScheduleDto.getTeacherId())
                    .teacherName(teacherScheduleDto.getTeacherName())
                    .pathFile(filePath)
                    .totalHour(teacherScheduleDto.getTotalHour())
                    .build();

            teacherSchedule = teacherScheduleRepository.save(teacherSchedule);
            return entityToDto(teacherSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create teacher schedule: " + e.getMessage());
        }
    }

    @Override
    public TeacherScheduleDto updateTeacherSchedule(Long id, TeacherScheduleDto teacherScheduleDto, MultipartFile file) {
        try {
            TeacherSchedule existingTeacherSchedule = teacherScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("TeacherSchedule not found with id " + id));

            if (file != null) {
                FileUtil.deleteFile(existingTeacherSchedule.getPathFile());
                String filePath = FileUtil.saveFile(uploadDir, file, "teacher-schedule");
                existingTeacherSchedule.setPathFile(filePath);
            }

            existingTeacherSchedule.setTeacherId(teacherScheduleDto.getTeacherId());
            existingTeacherSchedule.setTeacherName(teacherScheduleDto.getTeacherName());
            existingTeacherSchedule.setTotalHour(teacherScheduleDto.getTotalHour());

            existingTeacherSchedule = teacherScheduleRepository.save(existingTeacherSchedule);
            return entityToDto(existingTeacherSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update teacher schedule: " + e.getMessage());
        }
    }

    @Override
    public void deleteTeacherSchedule(Long id) {
        try {
            TeacherSchedule existingTeacherSchedule = teacherScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("TeacherSchedule not found with id " + id));

            FileUtil.deleteFile(existingTeacherSchedule.getPathFile());
            teacherScheduleRepository.delete(existingTeacherSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete teacher schedule: " + e.getMessage());
        }
    }

    @Override
    public List<TeacherScheduleDto> getAllTeacherSchedules() {
        try {
            List<TeacherSchedule> teacherSchedules = teacherScheduleRepository.findAll();
            return teacherSchedules.stream().map(this::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all teacher schedules: " + e.getMessage());
        }
    }

    @Override
    public TeacherScheduleDto getTeacherScheduleById(Long id) {
        try {
            TeacherSchedule teacherSchedule = teacherScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("TeacherSchedule not found with id " + id));
            return entityToDto(teacherSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get teacher schedule by id: " + e.getMessage());
        }
    }

    @Override
    public TeacherScheduleDto findByTeacher(String token) {
        try {
            String username = ExtractUsernameFromToken.extractUsernameFromToken(token);
            if (username == null || username.isEmpty()) {
                throw new RuntimeException("Invalid token: unable to extract username");
            }

            ResponseEntity<IamDto> responseEntity = iamClient.getUserByUsername(username);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                IamDto teacherDto = responseEntity.getBody();
                if (teacherDto == null || teacherDto.getId() == null)
                    throw new RuntimeException("Failed to retrieve teacher information from IAM");
                TeacherSchedule teacherSchedule = teacherScheduleRepository.findByTeacherId(teacherDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Teacher schedule not found for teacher ID: " + teacherDto.getId()));

                return entityToDto(teacherSchedule);
            } else {
                throw new RuntimeException("Failed to retrieve teacher information from IAM");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get teacher schedule: " + e.getMessage(), e);
        }

    }

    private TeacherScheduleDto entityToDto(TeacherSchedule teacherSchedule) {
        return new TeacherScheduleDto(
                teacherSchedule.getId(),
                teacherSchedule.getTeacherId(),
                teacherSchedule.getTeacherName(),
                teacherSchedule.getPathFile(),
                teacherSchedule.getTotalHour()
        );
    }
}
