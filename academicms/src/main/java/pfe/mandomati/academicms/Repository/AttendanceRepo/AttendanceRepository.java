package pfe.mandomati.academicms.Repository.AttendanceRepo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Attendance.Attendance;
import pfe.mandomati.academicms.Model.Student.Student;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

    List<Attendance> findByStudentProfile(Student studentProfile);
    
}
