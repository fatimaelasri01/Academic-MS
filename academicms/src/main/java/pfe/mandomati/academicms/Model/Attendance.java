package pfe.mandomati.academicms.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate date;
    
    @Column(nullable = false)
    private String status;
    
    private String justificationType;
    
    private String justificationDoc;
    
    private String validatorName;
    
    @ManyToOne
    @JoinColumn(name = "class_schedule_id")
    private ClassSchedule classSchedule;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student studentProfile;
}