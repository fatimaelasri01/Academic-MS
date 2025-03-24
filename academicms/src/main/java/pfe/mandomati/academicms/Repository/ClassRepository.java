package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Class;
import pfe.mandomati.academicms.Model.Filiere;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Long> {
    boolean existsByNumero(Integer numero);
    List<Class> findByFiliere(Filiere filiere);
}