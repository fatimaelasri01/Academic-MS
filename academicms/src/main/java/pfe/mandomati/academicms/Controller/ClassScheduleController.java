package pfe.mandomati.academicms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pfe.mandomati.academicms.Dto.ClassScheduleDto;
import pfe.mandomati.academicms.Service.ClassScheduleService;

import java.util.List;

@RestController
@RequestMapping("/class-schedule")
public class ClassScheduleController {

    @Autowired
    private ClassScheduleService classScheduleService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createClassSchedule(@RequestBody ClassScheduleDto classScheduleDto) {
        try {
            classScheduleService.createClassSchedule(classScheduleDto);
            return new ResponseEntity<>("Class schedule successfully created", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to create class schedule: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateClassSchedule(@PathVariable Long id, @RequestBody ClassScheduleDto classScheduleDto) {
        try {
            classScheduleService.updateClassSchedule(id, classScheduleDto);
            return new ResponseEntity<>("Class schedule successfully updated", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to update class schedule: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteClassSchedule(@PathVariable Long id) {
        try {
            classScheduleService.deleteClassSchedule(id);
            return new ResponseEntity<>("Class schedule successfully deleted", HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to delete class schedule: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<ClassScheduleDto>> getAllClassSchedules() {
        try {
            List<ClassScheduleDto> classSchedules = classScheduleService.getAllClassSchedules();
            return new ResponseEntity<>(classSchedules, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<ClassScheduleDto> getClassScheduleById(@PathVariable Long id) {
        try {
            ClassScheduleDto classSchedule = classScheduleService.getClassScheduleById(id);
            return new ResponseEntity<>(classSchedule, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}