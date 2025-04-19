package pfe.mandomati.academicms.Dto.ClassDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherAssignmentDto {
    private Long id;
    private Long teacherId;
    private String academicYear;
    private int hoursPerWeek;
    private Long classId;
    private Long subjectId; 
}
