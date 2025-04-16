package pfe.mandomati.academicms.Model.Attendance;

import pfe.mandomati.academicms.Model.Class.Class;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendance_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String studentId;
    
    @Column(nullable = false)
    private String academicYear;
    
    private int totalAbsent;
    
    private int totalLate;
    
    private int totalExcused;
    
    @OneToOne
    @JoinColumn(name = "class_id")
    private Class schoolClass; 
}
