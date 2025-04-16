package pfe.mandomati.academicms.Dto.LessonDto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonDto {
    private Long id;
    private String title;
    private String learningGoals;
    private Long teacherId;
    private String teacherName;
    private String className;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<FileInfoDto> files;
}
