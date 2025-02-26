package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}