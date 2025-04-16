package pfe.mandomati.academicms.Repository.LessonRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Lesson.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}