package pfe.mandomati.academicms.Service.ClassService;

import pfe.mandomati.academicms.Dto.ClassDto.SubjectDto;
import java.util.List;

public interface SubjectService {
    SubjectDto createSubject(SubjectDto subjectDto);
    SubjectDto updateSubject(Long subjectId, SubjectDto subjectDto);
    void deleteSubject(Long subjectId);
    List<SubjectDto> getAllSubjects();
    SubjectDto getSubjectById(Long subjectId);
    void assignSubjectToFiliere(Long subjectId, Long filiereId);
    void removeSubjectFromFiliere(Long subjectId, Long filiereId);
}