package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}