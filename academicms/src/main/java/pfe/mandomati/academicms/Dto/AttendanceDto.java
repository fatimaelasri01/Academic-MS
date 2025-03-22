package pfe.mandomati.academicms.Dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private Long id;
    private Long studentId;
    private LocalDate date;
    private String status;
    private String justificationType;
    private String justificationDoc;
    private String validatorName;
    private Long subjectId;
}
