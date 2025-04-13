package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Class;
import pfe.mandomati.academicms.Model.Filiere;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    boolean existsByNumero(Integer numero);
    List<Class> findByFiliere(Filiere filiere);
    Optional<Class> findByFiliereNameAndNumero(String filiereName, Integer numero);
    
}