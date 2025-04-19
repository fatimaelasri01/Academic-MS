package pfe.mandomati.academicms.Dto.ClassDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectDto {
    private Long subjectId;
    private String name;
    private String description;
    private String gradeLevel;
    private Integer coefficient;
    private String filiereName;
}