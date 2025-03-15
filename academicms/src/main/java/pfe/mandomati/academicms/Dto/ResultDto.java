package pfe.mandomati.academicms.Dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private Long id;
    private String studentId;
    private Double score;
    private String remarks;
    private String gradeLetter;
    private LocalDate gradeDate;
    private Long evaluationId;
}
