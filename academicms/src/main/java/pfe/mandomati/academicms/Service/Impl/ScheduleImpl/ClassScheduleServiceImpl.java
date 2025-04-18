package pfe.mandomati.academicms.Service.Impl.ScheduleImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pfe.mandomati.academicms.Dto.ScheduleDto.ClassScheduleDto;
import pfe.mandomati.academicms.Model.Schedule.ClassSchedule;
import pfe.mandomati.academicms.Repository.ScheduleRepo.ClassScheduleRepository;
import pfe.mandomati.academicms.Service.Impl.Utils.FileUtil;
import pfe.mandomati.academicms.Service.ScheduleService.ClassScheduleService;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ClassScheduleServiceImpl implements ClassScheduleService {


    private final ClassScheduleRepository classScheduleRepository;

    @Value("${file.upload.schedule-directory}")
    private String uploadDir; // Répertoire pour stocker les fichiers

    @Override
    public ClassScheduleDto createClassSchedule(ClassScheduleDto classScheduleDto, MultipartFile file) {
        try {
            // Sauvegarder le fichier
            String filePath = FileUtil.saveFile(uploadDir, file, "class-schedule");

            // Créer l'entité ClassSchedule
            ClassSchedule classSchedule = ClassSchedule.builder()
                    .className(classScheduleDto.getClassName())
                    .pathFile(filePath)
                    .totalHour(classScheduleDto.getTotalHour())
                    .build();

            classSchedule = classScheduleRepository.save(classSchedule);
            return entityToDto(classSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create class schedule: " + e.getMessage());
        }
    }

    @Override
    public ClassScheduleDto updateClassSchedule(Long id, ClassScheduleDto classScheduleDto, MultipartFile file) {
        try {
            ClassSchedule existingClassSchedule = classScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ClassSchedule not found with id " + id));

            // Supprimer l'ancien fichier si un nouveau fichier est uploadé
            if (file != null) {
                FileUtil.deleteFile(existingClassSchedule.getPathFile());
                String filePath = FileUtil.saveFile(uploadDir, file, "class-schedule");
                existingClassSchedule.setPathFile(filePath);
            }

            existingClassSchedule.setClassName(classScheduleDto.getClassName());
            existingClassSchedule.setTotalHour(classScheduleDto.getTotalHour());

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

            // Supprimer le fichier associé
            FileUtil.deleteFile(existingClassSchedule.getPathFile());

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
                classSchedule.getClassName(),
                classSchedule.getPathFile(),
                classSchedule.getTotalHour()
        );
    }
}