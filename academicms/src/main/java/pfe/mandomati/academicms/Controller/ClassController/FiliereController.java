package pfe.mandomati.academicms.Controller.ClassController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pfe.mandomati.academicms.Dto.ClassDto.FiliereDto;
import pfe.mandomati.academicms.Service.ClassService.FiliereService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filiere")
public class FiliereController {

    private final FiliereService filiereService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<FiliereDto> createFiliere(@RequestBody FiliereDto filiereDto) {
        FiliereDto createdFiliere = filiereService.createFiliere(filiereDto);
        return new ResponseEntity<>(createdFiliere, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<FiliereDto> updateFiliere(@PathVariable Long id, @RequestBody FiliereDto filiereDto) {
        FiliereDto updatedFiliere = filiereService.updateFiliere(id, filiereDto);
        return new ResponseEntity<>(updatedFiliere, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<Void> deleteFiliere(@PathVariable Long id) {
        filiereService.deleteFiliere(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<FiliereDto>> getAllFilieres() {
        List<FiliereDto> filieres = filiereService.getAllFilieres();
        return new ResponseEntity<>(filieres, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<FiliereDto> getFiliereById(@PathVariable Long id) {
        FiliereDto filiereDto = filiereService.getFiliereById(id);
        return new ResponseEntity<>(filiereDto, HttpStatus.OK);
    }

    @GetMapping("/all-with-subjects")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<FiliereDto>> getAllFilieresWithSubjects() {
        List<FiliereDto> filieres = filiereService.getAllFilieresWithSubjects();
        return new ResponseEntity<>(filieres, HttpStatus.OK);
    }
}