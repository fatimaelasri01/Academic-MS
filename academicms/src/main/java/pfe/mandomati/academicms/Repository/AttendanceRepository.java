package pfe.mandomati.academicms.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Attendance;
import pfe.mandomati.academicms.Model.StudentAcademicProfile;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

    List<Attendance> findByStudentProfile(StudentAcademicProfile studentProfile);
    
}
