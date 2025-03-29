package pfe.mandomati.academicms.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private Long id;
    private Long studentId;
    private LocalDate date;
    private String status;
    private boolean excused;
    private String justificationType;
    private String justificationDoc;
    private Long classScheduleId;
}