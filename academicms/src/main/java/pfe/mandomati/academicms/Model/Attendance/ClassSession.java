package pfe.mandomati.academicms.Model.Attendance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "class_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long classId; // ID de la classe

    @Column(nullable = false)
    private Long teacherId; // ID du professeur

    @Column(nullable = false)
    private Long subjectId; // ID de la matière

    @Column(nullable = false)
    private LocalDate sessionDate; // Date de la session

    @Column(nullable = false)
    private LocalTime startTime; // Heure de début de la session

    @Column(nullable = false)
    private LocalTime endTime; // Heure de fin de la session
}