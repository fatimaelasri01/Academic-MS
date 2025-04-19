package pfe.mandomati.academicms.Dto.ClassDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String speciality;
    
}
