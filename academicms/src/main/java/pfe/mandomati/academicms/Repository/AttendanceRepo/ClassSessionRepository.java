package pfe.mandomati.academicms.Repository.AttendanceRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Attendance.ClassSession;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {

}
