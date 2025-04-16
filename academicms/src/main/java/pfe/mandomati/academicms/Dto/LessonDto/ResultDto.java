package pfe.mandomati.academicms.Dto.LessonDto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDto {
    private Long id;
    private String studentId;
    private Double score;
    private String remarks;
    private String gradeLetter;
    private LocalDate gradeDate;
    private Long evaluationId;
}
