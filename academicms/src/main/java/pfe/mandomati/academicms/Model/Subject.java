package pfe.mandomati.academicms.Model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private String gradeLevel;
    
    private Integer coefficient;
    
    @ManyToMany(mappedBy = "subjects")
    private Set<Class> schoolClasses = new HashSet<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private Set<Lesson> lessons = new HashSet<>();
    
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private Set<Evaluation> evaluations = new HashSet<>();
}