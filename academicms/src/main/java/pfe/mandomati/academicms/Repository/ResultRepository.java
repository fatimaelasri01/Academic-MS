package pfe.mandomati.academicms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.academicms.Model.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
}