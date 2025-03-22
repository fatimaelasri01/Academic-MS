package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.mandomati.academicms.Dto.SubjectDto;
import pfe.mandomati.academicms.Model.Subject;
import pfe.mandomati.academicms.Repository.SubjectRepository;
import pfe.mandomati.academicms.Service.SubjectService;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public SubjectDto createSubject(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
        subject.setGradeLevel(subjectDto.getGradeLevel());
        subject.setCoefficient(subjectDto.getCoefficient());

        subject = subjectRepository.save(subject);
        return subjectToDto(subject);
    }

    @Override
    public SubjectDto updateSubject(Long subjectId, SubjectDto subjectDto) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + subjectId));
    
        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
        subject.setGradeLevel(subjectDto.getGradeLevel());
        subject.setCoefficient(subjectDto.getCoefficient());
    
        subject = subjectRepository.save(subject);
        return subjectToDto(subject);
    }
    
    @Override
    public void deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + subjectId));
        subjectRepository.delete(subject);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream().map(this::subjectToDto).collect(Collectors.toList());
    }

    @Override
    public SubjectDto getSubjectById(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + subjectId));
        return subjectToDto(subject);
    }

    public SubjectDto subjectToDto(Subject subject) {
        return new SubjectDto(
                subject.getSubjectId(),
                subject.getName(),
                subject.getDescription(),
                subject.getGradeLevel(),
                subject.getCoefficient()
        );
    }

    public Subject dtoToSubject(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
        subject.setGradeLevel(subjectDto.getGradeLevel());
        subject.setCoefficient(subjectDto.getCoefficient());
        return subject;
    }
}