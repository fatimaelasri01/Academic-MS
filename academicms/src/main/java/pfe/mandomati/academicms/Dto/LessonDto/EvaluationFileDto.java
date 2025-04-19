package pfe.mandomati.academicms.Dto.LessonDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.mandomati.academicms.Model.Lesson.EvaluationFile.EvaluationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluationFileDto {
    private Long id;
    private String name;
    private String filePath;
    private EvaluationType type;
    private String downloadUrl;
}
