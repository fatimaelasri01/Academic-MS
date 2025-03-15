package pfe.mandomati.academicms.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pfe.mandomati.academicms.Dto.ClassDto;
import pfe.mandomati.academicms.Exception.ClassCreationException;
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

            return new ClassDto(
                newClass.getId(),
                newClass.getName(),
                newClass.getAcademicYear(),
                newClass.getGradeLevel(),
                newClass.getCapacity(),
                newClass.getCreatedAt(),
                newClass.getUpdatedAt()
            );
        } catch (Exception e) {
            throw new ClassCreationException("Failed to create class", e);
        }
    }

    @Override
    @Transactional
    public ClassDto updateClass(Long id, ClassDto classDto) {
        Class existingClass = classRepository.findById(id)
                .orElseThrow(() -> new ClassCreationException("Class not found"));

        existingClass.setName(classDto.getName());
        existingClass.setAcademicYear(classDto.getAcademicYear());
        existingClass.setGradeLevel(classDto.getGradeLevel());
        existingClass.setCapacity(classDto.getCapacity());
        existingClass.setCreatedAt(classDto.getCreatedAt());
        existingClass.setUpdatedAt(classDto.getUpdatedAt());

        existingClass = classRepository.save(existingClass);

        return new ClassDto(
                existingClass.getId(),
                existingClass.getName(),
                existingClass.getAcademicYear(),
                existingClass.getGradeLevel(),
                existingClass.getCapacity(),
                existingClass.getCreatedAt(),
                existingClass.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    @Override
    public List<ClassDto> getAllClasses() {
        return classRepository.findAll().stream()
                .map(c -> new ClassDto(
                        c.getId(),
                        c.getName(),
                        c.getAcademicYear(),
                        c.getGradeLevel(),
                        c.getCapacity(),
                        c.getCreatedAt(),
                        c.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ClassDto getClassById(Long id) {
        Class c = classRepository.findById(id)
                .orElseThrow(() -> new ClassCreationException("Class not found"));
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

    @Override
    public ClassDto getClassByName(String name) {
        Class c = classRepository.findByName(name)
                .orElseThrow(() -> new ClassCreationException("Class not found"));
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