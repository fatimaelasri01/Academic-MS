package pfe.mandomati.academicms.Controller.ScheduleController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfe.mandomati.academicms.Dto.ScheduleDto.TeacherScheduleDto;
import pfe.mandomati.academicms.Service.ScheduleService.TeacherScheduleService;

import java.util.List;

@RestController
@RequestMapping("/teacher-schedules")
@RequiredArgsConstructor
public class TeacherScheduleController {

    private final TeacherScheduleService teacherScheduleService;

    @PostMapping
    public ResponseEntity<TeacherScheduleDto> createTeacherSchedule(
            @RequestPart("teacherSchedule") TeacherScheduleDto teacherScheduleDto,
            @RequestPart("file") MultipartFile file,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(teacherScheduleService.createTeacherSchedule(teacherScheduleDto, file, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherScheduleDto> updateTeacherSchedule(
            @PathVariable Long id,
            @RequestPart("teacherSchedule") TeacherScheduleDto teacherScheduleDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(teacherScheduleService.updateTeacherSchedule(id, teacherScheduleDto, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacherSchedule(@PathVariable Long id) {
        teacherScheduleService.deleteTeacherSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TeacherScheduleDto>> getAllTeacherSchedules() {
        return ResponseEntity.ok(teacherScheduleService.getAllTeacherSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherScheduleDto> getTeacherScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherScheduleService.getTeacherScheduleById(id));
    }

    @GetMapping("/by-teacher")
    public ResponseEntity<TeacherScheduleDto> getTeacherScheduleByTeacher(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(teacherScheduleService.getTeacherScheduleByTeacher(token));
    }
}