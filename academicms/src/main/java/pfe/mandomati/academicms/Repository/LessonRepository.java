package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}