package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.AttendanceSummary;
 

public interface AttendanceSummaryRepository extends JpaRepository<AttendanceSummary, Long>{
    
}
