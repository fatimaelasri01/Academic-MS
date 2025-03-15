package pfe.mandomati.academicms.Dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StudentDto extends IamDto {
    // Données académiques spécifiques
    private Long studentId; // Même ID que dans IAM-MS
    private String cne;
    private Long classId;
    private Date admissionDate; 
    private String academicStatus;
    private Long parentId;
    private String parentName;
    private String parentContact;
    private String parentemail;
    private List<AttendanceDto> attendanceRecords;
    private List<ResultDto> results;
    
}
