package pfe.mandomati.academicms.Controller.AttendanceController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pfe.mandomati.academicms.Dto.AttendanceDto.AttendanceSummaryDto;
import pfe.mandomati.academicms.Service.AttendanceService.AttendanceSummaryService;

import java.util.List;

@RestController
@RequestMapping("/attendance-summary")
public class AttendanceSummaryController {

    @Autowired
    private AttendanceSummaryService attendanceSummaryService;

    @Operation(summary = "Get attendance summary by student", description = "Retrieve attendance statistics (absent, late, excused) for a specific student.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved attendance summary"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<AttendanceSummaryDto>> getSummaryByStudent(@PathVariable Long studentId) {
        List<AttendanceSummaryDto> summary = attendanceSummaryService.getSummaryByStudent(studentId);
        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "Get attendance summary by class", description = "Retrieve attendance statistics (absent, late, excused) for a specific class.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved attendance summary"),
        @ApiResponse(responseCode = "404", description = "Class not found")
    })
    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<AttendanceSummaryDto>> getSummaryByClass(@PathVariable Long classId) {
        List<AttendanceSummaryDto> summary = attendanceSummaryService.getSummaryByClass(classId);
        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "Get top 3 classes with the most absences", description = "Retrieve the top 3 classes with the highest number of absences.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved top 3 classes with the most absences")
    })
    @GetMapping("/top-3-absent-classes")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<AttendanceSummaryDto>> getTop3ClassesWithMostAbsences() {
        List<AttendanceSummaryDto> topClasses = attendanceSummaryService.getTop3ClassesWithMostAbsences();
        return ResponseEntity.ok(topClasses);
    }

    @Operation(summary = "Get global attendance summary for an academic year", description = "Retrieve global attendance statistics (absent, late, excused) for a specific academic year.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved global attendance summary"),
        @ApiResponse(responseCode = "404", description = "No data found for the given academic year")
    })
    @GetMapping("/global-summary/{academicYear}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<AttendanceSummaryDto> getGlobalSummaryByAcademicYear(@PathVariable String academicYear) {
        AttendanceSummaryDto summary = attendanceSummaryService.getGlobalSummaryByAcademicYear(academicYear);
        return ResponseEntity.ok(summary);
    }


    @Operation(summary = "Get attendance summary by subject", description = "Retrieve attendance statistics (absent, late, excused) for a specific subject.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved attendance summary"),
        @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<AttendanceSummaryDto>> getSummaryBySubject(@PathVariable Long subjectId) {
        List<AttendanceSummaryDto> summary = attendanceSummaryService.getSummaryBySubject(subjectId);
        return ResponseEntity.ok(summary);
    }
    

    @Operation(summary = "Get attendance summary by teacher", description = "Retrieve attendance statistics (absent, late, excused) for a specific teacher.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved attendance summary"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    public ResponseEntity<List<AttendanceSummaryDto>> getSummaryByTeacher(@PathVariable Long teacherId) {
        List<AttendanceSummaryDto> summary = attendanceSummaryService.getSummaryByTeacher(teacherId);
        return ResponseEntity.ok(summary);
    }
}