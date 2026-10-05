package com.pedro.clientesapi.repository;

import com.pedro.clientesapi.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer>, JpaSpecificationExecutor<CustomerOrder> {
}
