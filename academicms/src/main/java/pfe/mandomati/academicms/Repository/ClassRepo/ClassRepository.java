package pfe.mandomati.academicms.Repository.ClassRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.academicms.Model.Class.Class;
import pfe.mandomati.academicms.Model.Class.Filiere;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    List<Class> findByFiliere(Filiere filiere);
    Optional<Class> findByFiliereNameAndNumero(String filiereName, Integer numero);
    
}