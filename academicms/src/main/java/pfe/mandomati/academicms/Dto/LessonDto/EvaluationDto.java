package pfe.mandomati.academicms.Dto.LessonDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.mandomati.academicms.Model.Class.TeacherAssignment;
import lombok.AllArgsConstructor;
import pfe.mandomati.academicms.Model.Lesson.EvaluationFile;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluationDto {
    private Long id;
    private String title;
    private Long teacherId;
    private String teacherName;
    private String description;
    private String ClassName;
    private Set<EvaluationFile> evaluationFiles;
}
