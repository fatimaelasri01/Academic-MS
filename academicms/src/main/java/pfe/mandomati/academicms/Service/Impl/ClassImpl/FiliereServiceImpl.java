package pfe.mandomati.academicms.Service.Impl.ClassImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pfe.mandomati.academicms.Dto.ClassDto.FiliereDto;
import pfe.mandomati.academicms.Dto.ClassDto.SubjectDto;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Class.Filiere;
import pfe.mandomati.academicms.Model.Class.Subject;
import pfe.mandomati.academicms.Repository.ClassRepo.FiliereRepository;
import pfe.mandomati.academicms.Service.ClassService.FiliereService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FiliereServiceImpl implements FiliereService {

    @Autowired
    private FiliereRepository filiereRepository;

    @Override
    @Transactional
    public FiliereDto createFiliere(FiliereDto filiereDto) {
        Filiere filiere = new Filiere();
        filiere.setName(filiereDto.getName());
        filiere.setDescription(filiereDto.getDescription());

        filiere = filiereRepository.save(filiere);
        return filiereToDto(filiere);
    }

    @Override
    @Transactional
    public FiliereDto updateFiliere(Long id, FiliereDto filiereDto) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filiere not found with id " + id));

        filiere.setName(filiereDto.getName());
        filiere.setDescription(filiereDto.getDescription());

        filiere = filiereRepository.save(filiere);
        return filiereToDto(filiere);
    }

    @Override
    @Transactional
    public void deleteFiliere(Long id) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filiere not found with id " + id));
        filiereRepository.delete(filiere);
    }

    @Override
    public List<FiliereDto> getAllFilieres() {
        List<Filiere> filieres = filiereRepository.findAll();
        return filieres.stream().map(this::filiereToDto).collect(Collectors.toList());
    }

    @Override
    public FiliereDto getFiliereById(Long id) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filiere not found with id " + id));
        return filiereToDto(filiere);
    }

    @Override
    public List<FiliereDto> getAllFilieresWithSubjects() {
        List<Filiere> filieres = filiereRepository.findAll();
        return filieres.stream().map(this::filiereToDtoWithSubjects).collect(Collectors.toList());
    }

    private FiliereDto filiereToDto(Filiere filiere) {
        return new FiliereDto(
                filiere.getId(),
                filiere.getName(),
                filiere.getDescription(),
                null // Pas de sujets ici
        );
    }

    private FiliereDto filiereToDtoWithSubjects(Filiere filiere) {
        List<SubjectDto> subjectDtos = filiere.getSubjects().stream()
                .map(this::subjectToDto)
                .collect(Collectors.toList());

        return new FiliereDto(
                filiere.getId(),
                filiere.getName(),
                filiere.getDescription(),
                subjectDtos
        );
    }

    private SubjectDto subjectToDto(Subject subject) {
        return new SubjectDto(
                subject.getSubjectId(),
                subject.getName(),
                subject.getDescription(),
                subject.getGradeLevel(),
                subject.getCoefficient(),
                subject.getFiliere().getName()
        );
    }
}