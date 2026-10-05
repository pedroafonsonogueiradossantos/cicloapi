package com.pedro.clientesapi.repository;

import com.pedro.clientesapi.dto.MaintenanceDueProjection;
import com.pedro.clientesapi.entity.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface MaintenanceRepository extends Repository<OrderItem, Integer> {
    @Query(value = """
            WITH last_purchase AS (
                SELECT
                    o.person_id AS person_id,
                    p.name AS person_name,
                    oi.item_id AS item_id,
                    i.name AS item_name,
                    o.id AS last_order_id,
                    COALESCE(o.order_date, o.created_at) AS effective_date,
                    oi.maintenance_interval_months AS maintenance_interval_months,
                    COALESCE(o.order_date, o.created_at) + (oi.maintenance_interval_months || ' months')::interval AS due_date,
                    ROW_NUMBER() OVER (
                        PARTITION BY o.person_id, oi.item_id
                        ORDER BY COALESCE(o.order_date, o.created_at) DESC
                    ) AS rn
                FROM order_item oi
                JOIN orders o ON o.id = oi.order_id
                JOIN item i ON i.id = oi.item_id
                JOIN person p ON p.id = o.person_id
                WHERE oi.maintenance_interval_months IS NOT NULL
            )
            SELECT
                lp.person_id AS personId,
                lp.person_name AS personName,
                lp.item_id AS itemId,
                lp.item_name AS itemName,
                lp.last_order_id AS lastOrderId,
                lp.effective_date AS lastOrderDate,
                lp.maintenance_interval_months AS maintenanceIntervalMonths,
                lp.due_date AS dueDate,
                STRING_AGG(DISTINCT ph.number, ', ') AS phoneNumbers
            FROM last_purchase lp
            LEFT JOIN phone ph ON ph.person_id = lp.person_id
            WHERE lp.rn = 1
              AND lp.due_date BETWEEN :from AND :to
            GROUP BY lp.person_id, lp.person_name, lp.item_id, lp.item_name, lp.last_order_id, lp.effective_date, lp.maintenance_interval_months, lp.due_date
            ORDER BY lp.due_date ASC
            """, nativeQuery = true)
    List<MaintenanceDueProjection> findDue(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);
}
