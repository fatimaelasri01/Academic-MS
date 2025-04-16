package pfe.mandomati.academicms.Model.Lesson;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lesson_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String filePath;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    public enum FileType {
        LESSON_CONTENT, 
        TD_CONTENT, 
        ADDITIONAL_RESOURCE
    }
}