package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Dto.SubjectDto;
import java.util.List;

public interface SubjectService {
    SubjectDto createSubject(SubjectDto subjectDto);
    SubjectDto updateSubject(Long subjectId, SubjectDto subjectDto);
    void deleteSubject(Long subjectId);
    List<SubjectDto> getAllSubjects();
    SubjectDto getSubjectById(Long subjectId);
    void assignSubjectToClass(Long subjectId, Long classId);
    void removeSubjectFromClass(Long subjectId, Long classId);
}