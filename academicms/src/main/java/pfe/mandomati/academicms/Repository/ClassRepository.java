package pfe.mandomati.academicms.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pfe.mandomati.academicms.Model.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {

    @Query("SELECT c.id FROM Class c WHERE c.name = :className")
    Long findIdByName(@Param("className") String className);

    Optional<Class> findByName(String name);
}