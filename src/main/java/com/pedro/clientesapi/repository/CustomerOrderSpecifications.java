package com.pedro.clientesapi.repository;

import com.pedro.clientesapi.entity.CustomerOrder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class CustomerOrderSpecifications {

    private CustomerOrderSpecifications() {
    }

    public static Specification<CustomerOrder> withFilters(Integer personId, OffsetDateTime startDate, OffsetDateTime endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (personId != null) {
                predicates.add(cb.equal(root.get("person").get("id"), personId));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("orderDate"), endDate));
            }
            if (!Long.class.equals(query.getResultType())) {
                root.fetch("person", JoinType.INNER);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
