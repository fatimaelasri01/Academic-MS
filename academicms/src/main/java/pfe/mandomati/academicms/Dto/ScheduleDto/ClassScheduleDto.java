package pfe.mandomati.academicms.Dto.ScheduleDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleDto {
    private Long id;
    private String className; // Nom de la classe (filiere + numero)
    private String pathFile;
    private Integer totalHour;
}