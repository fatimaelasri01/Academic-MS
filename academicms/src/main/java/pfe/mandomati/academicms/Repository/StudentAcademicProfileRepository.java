package pfe.mandomati.academicms.Repository;


import pfe.mandomati.academicms.Model.StudentAcademicProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StudentAcademicProfileRepository extends JpaRepository<StudentAcademicProfile, Long> {
    List<StudentAcademicProfile> findByClassId(Long classId);
    List<StudentAcademicProfile> findByAcademicStatus(String academicStatus);
    StudentAcademicProfile findByCne(String cne);
    List<StudentAcademicProfile> findByAdmissionDate(Date admissionDate);
    
    List<StudentAcademicProfile> findByParentEmail(String parentEmail);
    List<StudentAcademicProfile> findByParentId(Long parentId);
}