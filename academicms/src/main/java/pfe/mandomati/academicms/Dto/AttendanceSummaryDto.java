package pfe.mandomati.academicms.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSummaryDto {
    private Long id;
    private String studentId;
    private String academicYear;
    private int totalPresent;
    private int totalAbsent;
    private int totalLate;
    private int totalExcused;
    private Long classId;
}
