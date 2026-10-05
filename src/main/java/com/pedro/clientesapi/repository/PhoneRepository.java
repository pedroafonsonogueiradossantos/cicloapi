package com.pedro.clientesapi.repository;

import com.pedro.clientesapi.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {
    List<Phone> findByPersonId(Integer personId);
}
