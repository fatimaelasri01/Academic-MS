package pfe.mandomati.academicms.Dto.ScheduleDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherScheduleDto {
    private Long id;
    private Long teacherId;
    private String teacherName;
    private String pathFile;
    private Integer totalHour;
}
