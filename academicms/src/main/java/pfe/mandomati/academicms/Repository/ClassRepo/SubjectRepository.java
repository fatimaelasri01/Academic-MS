package pfe.mandomati.academicms.Repository.ClassRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Class.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
    
}
