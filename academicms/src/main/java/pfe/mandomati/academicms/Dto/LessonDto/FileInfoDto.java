package pfe.mandomati.academicms.Dto.LessonDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.mandomati.academicms.Model.Lesson.LessonFile.FileType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDto {
    private Long id;
    private String name;
    private FileType type;
    private String downloadUrl; 
}
