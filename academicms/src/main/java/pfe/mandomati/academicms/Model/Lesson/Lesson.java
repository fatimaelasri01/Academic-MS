package pfe.mandomati.academicms.Model.Lesson;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.mandomati.academicms.Model.Class.Subject;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;
    
    @Column(nullable = false)
    private String title;

    private String learningGoals;

    private String className;

    private String teacherName;

    private Long teacherId;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<LessonFile> files = new HashSet<>();   
}
