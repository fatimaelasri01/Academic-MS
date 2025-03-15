package pfe.mandomati.academicms.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.mandomati.academicms.Model.TeacherAssignment;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {
    private Long id;
    private int weightInPercent;
    private String title;
    private String description;
    private String evaluationType;
    private String notes;
    private Long subjectId;
    private TeacherAssignment teacher;
}
