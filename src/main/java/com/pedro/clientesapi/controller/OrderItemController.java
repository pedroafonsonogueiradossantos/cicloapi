package com.pedro.clientesapi.controller;

import com.pedro.clientesapi.dto.OrderItemRequest;
import com.pedro.clientesapi.dto.OrderItemResponse;
import com.pedro.clientesapi.entity.CustomerOrder;
import com.pedro.clientesapi.entity.Item;
import com.pedro.clientesapi.entity.OrderItem;
import com.pedro.clientesapi.exception.NotFoundException;
import com.pedro.clientesapi.repository.CustomerOrderRepository;
import com.pedro.clientesapi.repository.ItemRepository;
import com.pedro.clientesapi.repository.OrderItemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;
    private final CustomerOrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public List<OrderItemResponse> findAll(@RequestParam(required = false) Integer orderId) {
        List<OrderItem> items = orderId != null
                ? orderItemRepository.findByOrderId(orderId)
                : orderItemRepository.findAll();
        return items.stream().map(OrderItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public OrderItemResponse findById(@PathVariable Integer id) {
        return OrderItemResponse.from(getOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemResponse create(@Valid @RequestBody OrderItemRequest request) {
        OrderItem orderItem = new OrderItem();
        applyRequest(orderItem, request);
        return OrderItemResponse.from(orderItemRepository.save(orderItem));
    }

    @PutMapping("/{id}")
    @Transactional
    public OrderItemResponse update(@PathVariable Integer id, @Valid @RequestBody OrderItemRequest request) {
        OrderItem orderItem = getOrThrow(id);
        applyRequest(orderItem, request);
        return OrderItemResponse.from(orderItemRepository.save(orderItem));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!orderItemRepository.existsById(id)) {
            throw new NotFoundException("Item de pedido nao encontrado: " + id);
        }
        orderItemRepository.deleteById(id);
    }

    private void applyRequest(OrderItem orderItem, OrderItemRequest request) {
        CustomerOrder order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new NotFoundException("Pedido nao encontrado: " + request.orderId()));
        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new NotFoundException("Item nao encontrado: " + request.itemId()));
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setQuantity(request.quantity());
        orderItem.setUnitPrice(request.unitPrice());
        orderItem.setMaintenanceIntervalMonths(request.maintenanceIntervalMonths());
    }

    private OrderItem getOrThrow(Integer id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item de pedido nao encontrado: " + id));
    }
}
