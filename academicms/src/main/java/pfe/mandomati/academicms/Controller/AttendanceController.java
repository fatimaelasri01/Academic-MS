package pfe.mandomati.academicms.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import pfe.mandomati.academicms.Dto.AttendanceDto;
import pfe.mandomati.academicms.Service.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createAttendance(@RequestBody AttendanceDto attendanceDto) {
        AttendanceDto createdAttendance = attendanceService.createAttendance(attendanceDto);
        return ResponseEntity.ok("Attendance successfully created with ID: " + createdAttendance.getId());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAttendance(@RequestBody AttendanceDto attendanceDto) {
        AttendanceDto updatedAttendance = attendanceService.updateAttendance(attendanceDto.getId(), attendanceDto);
        return ResponseEntity.ok("Attendance successfully updated with ID: " + updatedAttendance.getId());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'TEACHER', , 'STUDENT', , 'PARENT')")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByStudentId(studentId));
    }   
}
