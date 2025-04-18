package pfe.mandomati.academicms.Dto.ScheduleDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherScheduleDto {
    private Long id;
    private Long teacherId;
    private String teacherName;
    private String pathFile;
    private Integer totalHour;
}
