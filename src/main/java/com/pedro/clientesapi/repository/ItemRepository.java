package com.pedro.clientesapi.repository;

import com.pedro.clientesapi.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
