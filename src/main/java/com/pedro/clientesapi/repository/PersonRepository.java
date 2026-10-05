package com.pedro.clientesapi.repository;

import com.pedro.clientesapi.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Page<Person> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
