package pfe.mandomati.academicms.Repository.ClassRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.mandomati.academicms.Model.Class.Filiere;
import java.util.Optional;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Long> {
    Optional<Filiere> findByName(String name);
}