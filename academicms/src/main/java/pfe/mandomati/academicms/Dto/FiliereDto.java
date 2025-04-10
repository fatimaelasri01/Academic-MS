package pfe.mandomati.academicms.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiliereDto {
    private Long id;
    private String name;
    private String description;
    private List<SubjectDto> subjects;
}