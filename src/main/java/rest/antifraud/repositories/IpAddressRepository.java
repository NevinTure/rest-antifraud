package rest.antifraud.repositories;

import rest.antifraud.models.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {

    boolean existsByAddress(String address);

    void deleteByAddress(String address);
}
