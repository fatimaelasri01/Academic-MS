package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Model.Lesson;
import java.util.List;

public interface LessonService {
    Lesson saveLesson(Lesson lesson);
    List<Lesson> getAllLessons();
    Lesson getLessonById(Long id);
    Lesson updateLesson(Lesson lesson);
    void deleteLesson(Long id);
}