package com.pedro.clientesapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    private String observation;

    @Column(name = "order_date")
    private OffsetDateTime orderDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "address_street")
    private String addressStreet;

    @Column(name = "address_number")
    private String addressNumber;

    @Column(name = "address_neighborhood")
    private String addressNeighborhood;

    @Column(name = "address_city_name")
    private String addressCityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_address_id")
    private Address originAddress;
}
