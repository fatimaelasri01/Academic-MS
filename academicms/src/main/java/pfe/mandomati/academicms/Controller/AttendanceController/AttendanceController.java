package pfe.mandomati.academicms.Controller.AttendanceController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import pfe.mandomati.academicms.Dto.AttendanceDto.AttendanceDto;
import pfe.mandomati.academicms.Service.AttendanceService.AttendanceService;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> createAttendance(@RequestBody AttendanceDto attendanceDto) {
        AttendanceDto createdAttendance = attendanceService.createAttendance(attendanceDto);
        return ResponseEntity.ok("Attendance successfully created with ID: " + createdAttendance.getId());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAttendance(@PathVariable Long id ,@RequestBody AttendanceDto attendanceDto) {
        AttendanceDto updatedAttendance = attendanceService.updateAttendance(id, attendanceDto);
        return ResponseEntity.ok("Attendance successfully updated with ID: " + updatedAttendance.getId());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok("Attendance successfully deleted with ID: " + id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<AttendanceDto>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<AttendanceDto> getAttendanceById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @GetMapping("/student/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'TEACHER', 'STUDENT', 'PARENT')")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByStudentId(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendancesByStudentId(id));
    }   
}
