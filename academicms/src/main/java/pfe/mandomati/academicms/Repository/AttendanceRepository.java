package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
    
}
