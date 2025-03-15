package pfe.mandomati.academicms.Dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.mandomati.academicms.Model.TeacherAssignment;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;
    private String lessonId;
    private String title;
    private String objective;
    private String content;
    private LocalDate createdAt;
    private Long subjectId;
    private TeacherAssignment teacher;
}
