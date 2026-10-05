package com.pedro.clientesapi.controller;

import com.pedro.clientesapi.dto.OrderRequest;
import com.pedro.clientesapi.dto.OrderResponse;
import com.pedro.clientesapi.dto.PageResponse;
import com.pedro.clientesapi.entity.Address;
import com.pedro.clientesapi.entity.CustomerOrder;
import com.pedro.clientesapi.entity.Person;
import com.pedro.clientesapi.exception.BadRequestException;
import com.pedro.clientesapi.exception.NotFoundException;
import com.pedro.clientesapi.repository.AddressRepository;
import com.pedro.clientesapi.repository.CustomerOrderRepository;
import com.pedro.clientesapi.repository.CustomerOrderSpecifications;
import com.pedro.clientesapi.repository.PersonRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CustomerOrderRepository orderRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    @GetMapping
    public PageResponse<OrderResponse> findAll(
            @RequestParam(required = false) Integer personId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CustomerOrder> orders = orderRepository.findAll(
                CustomerOrderSpecifications.withFilters(personId, startDate, endDate), pageable);
        return PageResponse.from(orders.map(OrderResponse::from));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public OrderResponse findById(@PathVariable Integer id) {
        return OrderResponse.from(getOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        CustomerOrder order = new CustomerOrder();
        applyRequest(order, request);
        return OrderResponse.from(orderRepository.save(order));
    }

    @PutMapping("/{id}")
    @Transactional
    public OrderResponse update(@PathVariable Integer id, @Valid @RequestBody OrderRequest request) {
        CustomerOrder order = getOrThrow(id);
        applyRequest(order, request);
        return OrderResponse.from(orderRepository.save(order));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Pedido nao encontrado: " + id);
        }
        orderRepository.deleteById(id);
    }

    private void applyRequest(CustomerOrder order, OrderRequest request) {
        Person person = personRepository.findById(request.personId())
                .orElseThrow(() -> new NotFoundException("Pessoa nao encontrada: " + request.personId()));
        order.setPerson(person);
        order.setObservation(request.observation());
        order.setOrderDate(request.orderDate());

        if (request.addressId() == null) {
            order.setOriginAddress(null);
            order.setAddressStreet(null);
            order.setAddressNumber(null);
            order.setAddressNeighborhood(null);
            order.setAddressCityName(null);
            return;
        }

        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new NotFoundException("Endereco nao encontrado: " + request.addressId()));
        if (!address.getPerson().getId().equals(person.getId())) {
            throw new BadRequestException("Endereco nao pertence ao cliente informado");
        }
        order.setOriginAddress(address);
        order.setAddressStreet(address.getStreet());
        order.setAddressNumber(address.getNumber());
        order.setAddressNeighborhood(address.getNeighborhood());
        order.setAddressCityName(address.getCity() != null ? address.getCity().getName() : null);
    }

    private CustomerOrder getOrThrow(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido nao encontrado: " + id));
    }
}
