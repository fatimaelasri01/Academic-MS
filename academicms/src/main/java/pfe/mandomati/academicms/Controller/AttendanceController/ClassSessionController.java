package pfe.mandomati.academicms.Controller.AttendanceController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.mandomati.academicms.Dto.AttendanceDto.ClassSessionDto;
import pfe.mandomati.academicms.Service.AttendanceService.ClassSessionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/class-session")
public class ClassSessionController {

    private final ClassSessionService classSessionService;

    @PostMapping("/create")
    public ResponseEntity<ClassSessionDto> createClassSession(@RequestBody ClassSessionDto classSessionDto) {
        return ResponseEntity.ok(classSessionService.createClassSession(classSessionDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClassSessionDto> updateClassSession(@PathVariable Long id, @RequestBody ClassSessionDto classSessionDto) {
        return ResponseEntity.ok(classSessionService.updateClassSession(id, classSessionDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteClassSession(@PathVariable Long id) {
        classSessionService.deleteClassSession(id);
        return ResponseEntity.ok("ClassSession deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassSessionDto> getClassSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(classSessionService.getClassSessionById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClassSessionDto>> getAllClassSessions() {
        return ResponseEntity.ok(classSessionService.getAllClassSessions());
    }
}