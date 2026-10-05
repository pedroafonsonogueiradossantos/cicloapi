package com.pedro.clientesapi.repository;

import com.pedro.clientesapi.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
}
