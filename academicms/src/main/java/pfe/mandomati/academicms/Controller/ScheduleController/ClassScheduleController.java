package pfe.mandomati.academicms.Controller.ScheduleController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfe.mandomati.academicms.Dto.ScheduleDto.ClassScheduleDto;
import pfe.mandomati.academicms.Service.ScheduleService.ClassScheduleService;

import java.util.List;

@RestController
@RequestMapping("/class-schedule")
public class ClassScheduleController {

    @Autowired
    private ClassScheduleService classScheduleService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassScheduleDto> createClassSchedule(
            @RequestPart("classSchedule") ClassScheduleDto classScheduleDto,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(classScheduleService.createClassSchedule(classScheduleDto, file));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassScheduleDto> updateClassSchedule(
            @PathVariable Long id,
            @RequestPart("classSchedule") ClassScheduleDto classScheduleDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(classScheduleService.updateClassSchedule(id, classScheduleDto, file));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteClassSchedule(@PathVariable Long id) {
        classScheduleService.deleteClassSchedule(id);
        return ResponseEntity.ok("Class schedule deleted successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<ClassScheduleDto>> getAllClassSchedules() {
        return ResponseEntity.ok(classScheduleService.getAllClassSchedules());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<ClassScheduleDto> getClassScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(classScheduleService.getClassScheduleById(id));
    }
}