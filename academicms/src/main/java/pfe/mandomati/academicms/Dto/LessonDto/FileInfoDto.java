package pfe.mandomati.academicms.Dto.LessonDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.mandomati.academicms.Model.Lesson.LessonFile.FileType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfoDto {
    private Long id;
    private String name;
    private FileType type;
    private String downloadUrl; 
}
