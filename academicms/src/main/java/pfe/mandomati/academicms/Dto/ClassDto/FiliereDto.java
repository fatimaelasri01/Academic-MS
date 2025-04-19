package pfe.mandomati.academicms.Dto.ClassDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FiliereDto {
    private Long id;
    private String name;
    private String description;
    private List<SubjectDto> subjects;
}