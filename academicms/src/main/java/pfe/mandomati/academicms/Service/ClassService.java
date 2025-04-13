package pfe.mandomati.academicms.Service;

import java.util.List;

import pfe.mandomati.academicms.Dto.ClassDto;

public interface ClassService {

    ClassDto addClass(ClassDto classDto);
    ClassDto updateClass(Long id, ClassDto classDto);
    void deleteClass(Long id);
    List<ClassDto> getAllClasses();
    ClassDto getClassById(Long id);
    List<ClassDto> getClassesByFiliere(String filiereName);
    ClassDto getClassByName(String filiereName, Integer numero);
}