package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.ClassSchedule;

public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
}