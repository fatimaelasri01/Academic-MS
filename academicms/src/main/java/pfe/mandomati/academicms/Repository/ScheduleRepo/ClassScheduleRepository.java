package pfe.mandomati.academicms.Repository.ScheduleRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Schedule.ClassSchedule;

public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
}