package pfe.mandomati.academicms.Model.Lesson;

import pfe.mandomati.academicms.Model.Class.Class;

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
    private Long Id;
    
    @Column(nullable = false)
    private String title;

    private Long teacherId;

    private String teacherName;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class schoolClass;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<EvaluationFile> evaluationFiles = new HashSet<>();


}
