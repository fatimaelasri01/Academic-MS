package pfe.mandomati.academicms.Service.LessonService;

import pfe.mandomati.academicms.Dto.LessonDto.LessonDto;

import java.util.List;

public interface LessonService {
    LessonDto createLesson(LessonDto lessonDto);
    LessonDto getLessonById(Long id);
    List<LessonDto> getAllLessons();
    LessonDto updateLesson(Long id, LessonDto lessonDto);
    void deleteLesson(Long id);
}