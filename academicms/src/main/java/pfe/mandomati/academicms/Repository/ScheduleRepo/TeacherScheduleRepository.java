package pfe.mandomati.academicms.Repository.ScheduleRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Schedule.TeacherSchedule;

import java.util.Optional;


public interface TeacherScheduleRepository extends JpaRepository<TeacherSchedule, Long> {
    Optional<TeacherSchedule> findByTeacherId(Long teacherId);
}
