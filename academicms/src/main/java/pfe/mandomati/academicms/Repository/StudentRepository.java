package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}