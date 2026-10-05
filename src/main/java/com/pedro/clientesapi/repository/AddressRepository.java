package com.pedro.clientesapi.repository;

import com.pedro.clientesapi.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByPersonId(Integer personId);
}
