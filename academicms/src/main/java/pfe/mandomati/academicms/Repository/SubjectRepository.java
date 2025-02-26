package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
    
}
