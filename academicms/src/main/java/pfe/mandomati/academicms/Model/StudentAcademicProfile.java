package pfe.mandomati.academicms.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "student_academic_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAcademicProfile {
    @Id
    @Column(name = "student_id")
    private Long studentId; // Même ID que dans IAM-MS
    
    @Column(nullable = false, unique = true)
    private String cne;

    @Column(nullable = false)
    private Long classId;

    private Date admissionDate; 
    private String academicStatus;
    private boolean assurance;

    @Column(nullable = false)
    private Long parentId;
    
    private String parentName;
    private String parentContact;
    private String parentEmail;
}
