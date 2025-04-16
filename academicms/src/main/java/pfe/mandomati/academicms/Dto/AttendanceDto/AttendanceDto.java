package pfe.mandomati.academicms.Dto.AttendanceDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendanceDto {
    private Long id;
    private Long studentId;
    private String status;
    private boolean excused;
    private String justificationType;
    private String justificationDoc;
    private Long classSessionId;
}