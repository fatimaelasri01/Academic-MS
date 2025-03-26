package pfe.mandomati.academicms.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;

//import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassDto {
    private Long classId;
    private String filiereName;
    private Integer numero;
    private String academicYear;
    private String gradeLevel;
    private Integer capacity;
    //private Date createdAt;
    //private Date updatedAt;
}