package pfe.mandomati.academicms.Dto.AttendanceDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendanceSummaryDto {
    private Long id;
    private String studentId;
    private String academicYear;
    private int totalAbsent;
    private int totalLate;
    private int totalExcused;
    private Long classId;
}
