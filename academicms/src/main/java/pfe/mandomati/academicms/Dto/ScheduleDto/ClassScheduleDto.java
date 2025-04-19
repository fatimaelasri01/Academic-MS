package pfe.mandomati.academicms.Dto.ScheduleDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassScheduleDto {
    private Long id;
    private String className; // Nom de la classe (filiere + numero)
    private String pathFile;
    private Integer totalHour;
}