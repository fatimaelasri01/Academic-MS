package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pfe.mandomati.academicms.Dto.ClassDto;
import pfe.mandomati.academicms.Exception.ClassCreationException;
import pfe.mandomati.academicms.Model.Class;
import pfe.mandomati.academicms.Model.Filiere;
import pfe.mandomati.academicms.Repository.ClassRepository;
import pfe.mandomati.academicms.Repository.FiliereRepository;
import pfe.mandomati.academicms.Service.ClassService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    @Override
    @Transactional
    public ClassDto addClass(ClassDto classDto) {
        Filiere filiere = filiereRepository.findByName(classDto.getFiliereName())
                .orElseThrow(() -> new ClassCreationException("Filiere not found"));

        /*érifier si le numéro de la classe est unique
        if (classRepository.existsByNumero(classDto.getNumero())) {
            throw new ClassAlreadyExistsException("Class with this number already exists");
        }*/

        try {
            Class newClass = Class.builder()
                    .filiere(filiere)
                    .academicYear(classDto.getAcademicYear())
                    .numero(classDto.getNumero())
                    .gradeLevel(classDto.getGradeLevel())
                    .build();

            newClass = classRepository.save(newClass);

            return classToDto(newClass);
        } catch (Exception e) {
            throw new ClassCreationException("Failed to create class", e);
        }
    }

    @Override
    @Transactional
    public ClassDto updateClass(Long id, ClassDto classDto) {
        Class existingClass = classRepository.findById(id)
                .orElseThrow(() -> new ClassCreationException("Class not found"));

        Filiere filiere = filiereRepository.findByName(classDto.getFiliereName())
                .orElseThrow(() -> new ClassCreationException("Filiere not found"));

        /*  Vérifier si le numéro de la classe est unique
        if (!existingClass.getNumero().equals(classDto.getNumero()) && classRepository.existsByNumero(classDto.getNumero())) {
            throw new ClassAlreadyExistsException("Class with this number already exists");
        }*/

        existingClass.setFiliere(filiere);
        existingClass.setAcademicYear(classDto.getAcademicYear());
        existingClass.setNumero(classDto.getNumero());
        existingClass.setGradeLevel(classDto.getGradeLevel());
        existingClass.setCapacity(classDto.getCapacity());

        existingClass = classRepository.save(existingClass);

        return classToDto(existingClass);
    }

    @Override
    @Transactional
    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    @Override
    public List<ClassDto> getAllClasses() {
        return classRepository.findAll().stream()
                .map(this::classToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClassDto getClassById(Long id) {
        Class c = classRepository.findById(id)
                .orElseThrow(() -> new ClassCreationException("Class not found"));
        return classToDto(c);
    }

    @Override
    public List<ClassDto> getClassesByFiliere(String filiereName) {
        Filiere filiere = filiereRepository.findByName(filiereName)
                .orElseThrow(() -> new ClassCreationException("Filiere not found"));
        return classRepository.findByFiliere(filiere).stream()
                .map(this::classToDto)
                .collect(Collectors.toList());
    }

    private ClassDto classToDto(Class c) {
        return ClassDto.builder()
                .classId(c.getId())
                .filiereName(c.getFiliere().getName())
                .numero(c.getNumero())
                .academicYear(c.getAcademicYear())
                .gradeLevel(c.getGradeLevel())
                .capacity(c.getCapacity())
                .build();
    }
}