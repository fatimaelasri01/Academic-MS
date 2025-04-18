package pfe.mandomati.academicms.Dto.StudentDto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import pfe.mandomati.academicms.Dto.AttendanceDto.AttendanceDto;
import pfe.mandomati.academicms.Dto.LessonDto.ResultDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDto extends IamDto{
    // Données académiques spécifiques
    //private Long studentId; // Même ID que dans IAM-MS
    private String cne;
    private String className;
    private Date admissionDate; 
    private String academicStatus;
    private boolean assurance;
    private Long parentId;
    private String parentName;
    private String parentContact;
    private String parentEmail;
    private List<AttendanceDto> attendanceRecords;
    private List<ResultDto> results;
    
}
