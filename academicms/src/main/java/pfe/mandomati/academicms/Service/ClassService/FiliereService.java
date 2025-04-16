package pfe.mandomati.academicms.Service.ClassService;

import pfe.mandomati.academicms.Dto.ClassDto.FiliereDto;

import java.util.List;

public interface FiliereService {
    FiliereDto createFiliere(FiliereDto filiereDto);
    FiliereDto updateFiliere(Long id, FiliereDto filiereDto);
    void deleteFiliere(Long id);
    List<FiliereDto> getAllFilieres();
    FiliereDto getFiliereById(Long id);
    List<FiliereDto> getAllFilieresWithSubjects(); // Ajout de la méthode
}