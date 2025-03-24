package pfe.mandomati.academicms.Model;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "class")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "filiere_id", nullable = false)
    private Filiere filiere;

    private Integer numero;

    @Column(nullable = false)
    private String academicYear;
    
    @Column(nullable = false)
    private String gradeLevel;
    
    private Integer capacity;

    private Date createdAt;

    private Date updatedAt;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    private Set<Student> studentAcademicProfiles = new HashSet<>();

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    private Set<TeacherAssignment> teacherAssignments = new HashSet<>();
}