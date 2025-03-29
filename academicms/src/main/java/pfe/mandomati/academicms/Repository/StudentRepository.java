package pfe.mandomati.academicms.Repository;


import pfe.mandomati.academicms.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.mandomati.academicms.Model.Class;

import java.util.Date;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findBySchoolClass(Class schoolClass);

    List<Student> findByAcademicStatus(String academicStatus);
    Student findByCne(String cne);
    List<Student> findByAdmissionDate(Date admissionDate);
    
    List<Student> findByParentEmail(String parentEmail);
    List<Student> findByParentId(Long parentId);
}