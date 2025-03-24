package pfe.mandomati.academicms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassWithSubjectsDto {
    private Long classId;
    private String className;
    private Set<SubjectDto> subjects;
}