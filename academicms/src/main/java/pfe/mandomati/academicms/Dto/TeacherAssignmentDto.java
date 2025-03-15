package pfe.mandomati.academicms.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAssignmentDto {
    private Long id;
    private Long teacherId;
    private String academicYear;
    private int hoursPerWeek;
    private String comments;
    private Long classId;
    private Long subjectId; 
}
