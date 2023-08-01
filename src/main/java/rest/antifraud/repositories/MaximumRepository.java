package rest.antifraud.repositories;

import rest.antifraud.models.Maximum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaximumRepository extends JpaRepository<Maximum, String> {
}
