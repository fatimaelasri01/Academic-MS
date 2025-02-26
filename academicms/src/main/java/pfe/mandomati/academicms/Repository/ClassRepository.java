package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {
}