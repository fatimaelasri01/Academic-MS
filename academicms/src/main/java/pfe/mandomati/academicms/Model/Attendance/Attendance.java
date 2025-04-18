package pfe.mandomati.academicms.Model.Attendance;

import pfe.mandomati.academicms.Model.Student.Student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String status;

    @Builder.Default
    private boolean excused = false;
    
    private String justificationType;
    
    private String justificationDoc;
    
    @ManyToOne
    @JoinColumn(name = "class_session_id")
    private ClassSession classSession;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student studentProfile;
}