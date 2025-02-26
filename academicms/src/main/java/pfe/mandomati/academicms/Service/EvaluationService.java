package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Model.Evaluation;
import java.util.List;

public interface EvaluationService {
    Evaluation saveEvaluation(Evaluation evaluation);
    List<Evaluation> getAllEvaluations();
    Evaluation getEvaluationById(Long id);
    Evaluation updateEvaluation(Evaluation evaluation);
    void deleteEvaluation(Long id);
}