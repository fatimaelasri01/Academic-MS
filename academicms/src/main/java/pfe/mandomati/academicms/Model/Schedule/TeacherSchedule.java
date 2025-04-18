package pfe.mandomati.academicms.Model.Schedule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teacher_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long teacherId;

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private String pathFile;

    private Integer totalHour;
}