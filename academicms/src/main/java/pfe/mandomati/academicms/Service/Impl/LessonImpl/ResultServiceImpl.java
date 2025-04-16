package pfe.mandomati.academicms.Service.Impl.LessonImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Model.Lesson.Result;
import pfe.mandomati.academicms.Repository.LessonRepo.ResultRepository;
import pfe.mandomati.academicms.Service.LessonService.ResultService;

import java.util.List;
import java.util.Optional;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }

    @Override
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    @Override
    public Result getResultById(Long id) {
        Optional<Result> optionalResult = resultRepository.findById(id);
        return optionalResult.orElse(null);
    }

    @Override
    public Result updateResult(Result result) {
        return resultRepository.save(result);
    }

    @Override
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }
}