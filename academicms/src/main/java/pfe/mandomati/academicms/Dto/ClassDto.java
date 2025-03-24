package pfe.mandomati.academicms.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDto {
    private Long classId;
    private String filiereName;
    private Integer numero;
    private String academicYear;
    private String gradeLevel;
    private Integer capacity;
    private Date createdAt;
    private Date updatedAt;
}