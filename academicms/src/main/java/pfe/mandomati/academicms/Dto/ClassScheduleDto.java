package pfe.mandomati.academicms.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleDto {
    private Long id;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;
    private String notes;
    private Long subjectId;
    private Long teacherId;
}
