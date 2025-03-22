package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pfe.mandomati.academicms.Dto.ClassDto;
import pfe.mandomati.academicms.Exception.ClassCreationException;
import pfe.mandomati.academicms.Exception.ClassAlreadyExistsException;
import pfe.mandomati.academicms.Model.Class;
import pfe.mandomati.academicms.Repository.ClassRepository;
import pfe.mandomati.academicms.Service.ClassService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Override
    @Transactional
    public ClassDto addClass(ClassDto classDto) {
        if (classRepository.findByName(classDto.getName()).isPresent()) {
            throw new ClassAlreadyExistsException("Class with name " + classDto.getName() + " already exists");
        }

        try {
            Class newClass = Class.builder()
                    .name(classDto.getName())
                    .academicYear(classDto.getAcademicYear())
                    .gradeLevel(classDto.getGradeLevel())
                    .capacity(classDto.getCapacity())
                    .createdAt(classDto.getCreatedAt())
                    .updatedAt(classDto.getUpdatedAt())
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

        if (!existingClass.getName().equals(classDto.getName()) && classRepository.findByName(classDto.getName()).isPresent()) {
            throw new ClassAlreadyExistsException("Class with name " + classDto.getName() + " already exists");
        }

        existingClass.setName(classDto.getName());
        existingClass.setAcademicYear(classDto.getAcademicYear());
        existingClass.setGradeLevel(classDto.getGradeLevel());
        existingClass.setCapacity(classDto.getCapacity());
        existingClass.setCreatedAt(classDto.getCreatedAt());
        existingClass.setUpdatedAt(classDto.getUpdatedAt());

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
    public ClassDto getClassByName(String name) {
        Class c = classRepository.findByName(name)
                .orElseThrow(() -> new ClassCreationException("Class not found"));
        return classToDto(c);
    }

    private ClassDto classToDto(Class c) {
        return new ClassDto(
                c.getId(),
                c.getName(),
                c.getAcademicYear(),
                c.getGradeLevel(),
                c.getCapacity(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}