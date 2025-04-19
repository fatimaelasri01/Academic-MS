package pfe.mandomati.academicms.Dto.ClassDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassWithSubjectsDto {
    private Long classId;
    private String className;
    private Set<SubjectDto> subjects;
}