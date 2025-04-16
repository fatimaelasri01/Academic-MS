package pfe.mandomati.academicms.Model.Lesson;

import pfe.mandomati.academicms.Model.Class.Subject;
import pfe.mandomati.academicms.Model.Class.TeacherAssignment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Long evaluationId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String evaluationType;
    
    private String notes;
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private Set<Result> results = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherAssignment teacher;
}
