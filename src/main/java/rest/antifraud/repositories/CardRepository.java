package rest.antifraud.repositories;

import rest.antifraud.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByNumber(String number);

    void deleteByNumber(String number);
}
