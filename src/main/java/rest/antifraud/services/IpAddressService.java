package rest.antifraud.services;

import rest.antifraud.models.IpAddress;
import rest.antifraud.repositories.IpAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IpAddressService {

    private final IpAddressRepository ipAddressRepository;

    @Autowired
    public IpAddressService(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    public void save(IpAddress ipAddress) {
        ipAddressRepository.save(ipAddress);
    }

    public List<IpAddress> getAll() {
        return ipAddressRepository.findAll();
    }

    public void deleteByAddress(String address) {
        ipAddressRepository.deleteByAddress(address);
    }

    public boolean existsByAddress(String address) {
        return ipAddressRepository.existsByAddress(address);
    }
}
