package pfe.mandomati.academicms.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teacher_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAssignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, name = "teacher_id")
    private Long teacherId;
    
    @Column(nullable = false)
    private String academicYear;
    
    private int hoursPerWeek;
    
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class schoolClass;
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
}
