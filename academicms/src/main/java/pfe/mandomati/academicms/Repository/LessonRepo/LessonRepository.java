package pfe.mandomati.academicms.Repository.LessonRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Lesson.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}