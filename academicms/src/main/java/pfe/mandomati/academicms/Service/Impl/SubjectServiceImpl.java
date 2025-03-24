package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pfe.mandomati.academicms.Dto.SubjectDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Filiere;
import pfe.mandomati.academicms.Model.Subject;
import pfe.mandomati.academicms.Repository.FiliereRepository;
import pfe.mandomati.academicms.Repository.SubjectRepository;
import pfe.mandomati.academicms.Service.SubjectService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    @Override
    @Transactional
    public SubjectDto createSubject(SubjectDto subjectDto) {
        Filiere filiere = filiereRepository.findByName(subjectDto.getFiliereName())
                .orElseThrow(() -> new ResourceNotFoundException("Filiere not found with name " + subjectDto.getFiliereName()));

        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
        subject.setGradeLevel(subjectDto.getGradeLevel());
        subject.setCoefficient(subjectDto.getCoefficient());
        subject.setFiliere(filiere);

        subject = subjectRepository.save(subject);
        return subjectToDto(subject);
    }

    @Override
    @Transactional
    public SubjectDto updateSubject(Long subjectId, SubjectDto subjectDto) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + subjectId));

        Filiere filiere = filiereRepository.findByName(subjectDto.getFiliereName())
                .orElseThrow(() -> new ResourceNotFoundException("Filiere not found with name " + subjectDto.getFiliereName()));

        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
        subject.setGradeLevel(subjectDto.getGradeLevel());
        subject.setCoefficient(subjectDto.getCoefficient());
        subject.setFiliere(filiere);

        subject = subjectRepository.save(subject);
        return subjectToDto(subject);
    }

    @Override
    @Transactional
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

    @Transactional
    public void assignSubjectToFiliere(Long subjectId, Long filiereId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject ID"));
        Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid filiere ID"));

        subject.setFiliere(filiere);
        subjectRepository.save(subject);
    }

    @Transactional
    public void removeSubjectFromFiliere(Long subjectId, Long filiereId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject ID"));
        Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid filiere ID"));

        if (subject.getFiliere().equals(filiere)) {
            subject.setFiliere(null);
            subjectRepository.save(subject);
        } else {
            throw new IllegalArgumentException("Subject not assigned to the filiere");
        }
    }

    private SubjectDto subjectToDto(Subject subject) {
        return new SubjectDto(
                subject.getSubjectId(),
                subject.getName(),
                subject.getDescription(),
                subject.getGradeLevel(),
                subject.getCoefficient(),
                subject.getFiliere().getName() // Utilisation de filiereName
        );
    }

    public Subject dtoToSubject(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setSubjectId(subjectDto.getSubjectId());
        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
        subject.setGradeLevel(subjectDto.getGradeLevel());
        subject.setCoefficient(subjectDto.getCoefficient());
        return subject;
    }
}