package pfe.mandomati.academicms.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private Long subjectId;
    private String name;
    private String description;
    private String gradeLevel;
    private Integer coefficient;
}
