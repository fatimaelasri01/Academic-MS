package pfe.mandomati.academicms.Service.Impl.LessonImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Model.Lesson.Lesson;
import pfe.mandomati.academicms.Dto.LessonDto.LessonDto;
import pfe.mandomati.academicms.Repository.LessonRepo.LessonRepository;
import pfe.mandomati.academicms.Service.LessonService.LessonService;

import org.springframework.transaction.annotation.Transactional;

import pfe.mandomati.academicms.Exception.ResourceNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {
        Lesson lesson = new Lesson();
        mapDtoToEntity(lessonDto, lesson);
        Lesson savedLesson = lessonRepository.save(lesson);
        return mapEntityToDto(savedLesson);
    }

    @Override
    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        return mapEntityToDto(lesson);
    }

    @Override
    public List<LessonDto> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        mapDtoToEntity(lessonDto, lesson);
        Lesson updatedLesson = lessonRepository.save(lesson);
        return mapEntityToDto(updatedLesson);
    }
    
    @Override
    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        
        // Delete all LessonFiles associated with this Lesson
        lesson.getFiles().forEach(lessonFile -> {
            try {
                Files.deleteIfExists(Paths.get(lessonFile.getFilePath())); // Delete the file from the file system
            } catch (IOException e) {
                throw new RuntimeException("Could not delete the file: " + lessonFile.getFilePath(), e);
            }
        });
        lesson.getFiles().clear(); // Clear the relationship to avoid cascading issues
    
        // Delete the Lesson
        lessonRepository.delete(lesson);
    }

    private LessonDto mapEntityToDto(Lesson lesson) {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(lesson.getId());
        lessonDto.setTitle(lesson.getTitle());
        lessonDto.setLearningGoals(lesson.getLearningGoals());
        lessonDto.setTeacherId(lesson.getTeacherId());
        lessonDto.setTeacherName(lesson.getTeacherName());
        lessonDto.setClassName(lesson.getClassName());
        return lessonDto;
    }

    private void mapDtoToEntity(LessonDto lessonDto, Lesson lesson) {
        lesson.setTitle(lessonDto.getTitle());
        lesson.setLearningGoals(lessonDto.getLearningGoals());
        lesson.setTeacherId(lessonDto.getTeacherId());
        lesson.setTeacherName(lessonDto.getTeacherName());
        lesson.setClassName(lessonDto.getClassName());
    }
}