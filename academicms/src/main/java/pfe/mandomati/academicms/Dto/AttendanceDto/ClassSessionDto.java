package pfe.mandomati.academicms.Dto.AttendanceDto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassSessionDto {
    private Long id;
    private Long classId;
    private Long teacherId;
    private Long subjectId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;  
}
