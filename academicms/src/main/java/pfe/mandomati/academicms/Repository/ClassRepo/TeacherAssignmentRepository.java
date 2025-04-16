package pfe.mandomati.academicms.Repository.ClassRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Class.TeacherAssignment;

public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, Long> {
}