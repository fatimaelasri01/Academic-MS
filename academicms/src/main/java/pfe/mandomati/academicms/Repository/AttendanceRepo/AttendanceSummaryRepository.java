package pfe.mandomati.academicms.Repository.AttendanceRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Attendance.AttendanceSummary;
 

public interface AttendanceSummaryRepository extends JpaRepository<AttendanceSummary, Long>{
    
}
