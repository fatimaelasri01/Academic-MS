package pfe.mandomati.academicms.Repository.LessonRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Lesson.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
}