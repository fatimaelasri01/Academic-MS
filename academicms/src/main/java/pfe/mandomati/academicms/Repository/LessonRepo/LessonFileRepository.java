package pfe.mandomati.academicms.Repository.LessonRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Lesson.LessonFile;

public interface LessonFileRepository extends JpaRepository<LessonFile, Long> {

    List<LessonFile> findByLessonId(Long lessonId);
    Optional<LessonFile> findByIdAndLessonId(Long id, Long lessonId); 
}
