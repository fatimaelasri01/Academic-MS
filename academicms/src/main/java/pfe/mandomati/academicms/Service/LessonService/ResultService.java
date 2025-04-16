package pfe.mandomati.academicms.Service.LessonService;

import pfe.mandomati.academicms.Model.Lesson.Result;
import java.util.List;

public interface ResultService {
    Result saveResult(Result result);
    List<Result> getAllResults();
    Result getResultById(Long id);
    Result updateResult(Result result);
    void deleteResult(Long id);
}