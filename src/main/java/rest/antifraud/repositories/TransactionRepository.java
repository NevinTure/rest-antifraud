package rest.antifraud.repositories;

import rest.antifraud.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select distinct t.ipAddress" +
            " from Transaction t" +
            " where t.cardNumber = :number and t.dateTime between :from and :to")
    List<String> findDistinctIpByDateTimeBetween(
            @Param("number") String number,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select distinct t.region" +
            " from Transaction t" +
            " where t.cardNumber = :number and t.dateTime between :from and :to")
    List<String> findDistinctRegionByDateTimeBetween(
            @Param("number") String number,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    List<Transaction> findAllByCardNumber(String cardNumber);
}
