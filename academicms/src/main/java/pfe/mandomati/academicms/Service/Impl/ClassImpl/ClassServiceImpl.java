package pfe.mandomati.academicms.Service.Impl.ClassImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pfe.mandomati.academicms.Dto.ClassDto.ClassDto;
import pfe.mandomati.academicms.Exception.ClassCreationException;
import pfe.mandomati.academicms.Exception.ResourceNotFoundException;
import pfe.mandomati.academicms.Model.Class.Class;
import pfe.mandomati.academicms.Model.Class.Filiere;
import pfe.mandomati.academicms.Repository.ClassRepo.ClassRepository;
import pfe.mandomati.academicms.Repository.ClassRepo.FiliereRepository;
import pfe.mandomati.academicms.Service.ClassService.ClassService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;

    private final FiliereRepository filiereRepository;

    @Override
    @Transactional
    public ClassDto addClass(ClassDto classDto) {
        Filiere filiere = filiereRepository.findByName(classDto.getFiliereName())
                .orElseThrow(() -> new ClassCreationException("Filiere not found"));

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

    @Override
    public ClassDto getClassByName(String filiereName, Integer numero) {
    Class classEntity = classRepository.findByFiliereNameAndNumero(filiereName, numero)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found with filiereName: " + filiereName + " and numero: " + numero));
        return classToDto(classEntity);
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