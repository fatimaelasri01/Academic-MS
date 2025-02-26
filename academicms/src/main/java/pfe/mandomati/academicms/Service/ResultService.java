package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Model.Result;
import java.util.List;

public interface ResultService {
    Result saveResult(Result result);
    List<Result> getAllResults();
    Result getResultById(Long id);
    Result updateResult(Result result);
    void deleteResult(Long id);
}