package pfe.mandomati.academicms.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Attendance;
import pfe.mandomati.academicms.Model.StudentAcademicProfile;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

    Optional<Attendance> findByStudentProfile(StudentAcademicProfile studentProfile);
    
}
