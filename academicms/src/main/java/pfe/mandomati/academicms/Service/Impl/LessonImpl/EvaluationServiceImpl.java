package pfe.mandomati.academicms.Service.Impl.LessonImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Model.Lesson.Evaluation;
import pfe.mandomati.academicms.Repository.LessonRepo.EvaluationRepository;
import pfe.mandomati.academicms.Service.LessonService.EvaluationService;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public Evaluation saveEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    @Override
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    @Override
    public Evaluation getEvaluationById(Long id) {
        Optional<Evaluation> optionalEvaluation = evaluationRepository.findById(id);
        return optionalEvaluation.orElse(null);
    }

    @Override
    public Evaluation updateEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    @Override
    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }
}