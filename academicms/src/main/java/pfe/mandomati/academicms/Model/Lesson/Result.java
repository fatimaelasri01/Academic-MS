package pfe.mandomati.academicms.Model.Lesson;

import pfe.mandomati.academicms.Model.Student.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double score;
    
    private String remarks;
    
    private String gradeLetter;
    
    private LocalDate gradeDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student studentProfile;
}
