package pfe.mandomati.academicms.Service;

import pfe.mandomati.academicms.Dto.FiliereDto;

import java.util.List;

public interface FiliereService {
    FiliereDto createFiliere(FiliereDto filiereDto);
    FiliereDto updateFiliere(Long id, FiliereDto filiereDto);
    void deleteFiliere(Long id);
    List<FiliereDto> getAllFilieres();
    FiliereDto getFiliereById(Long id);
    List<FiliereDto> getAllFilieresWithSubjects(); // Ajout de la méthode
}