package com.pedro.clientesapi.controller;

import com.pedro.clientesapi.dto.ItemRequest;
import com.pedro.clientesapi.dto.ItemResponse;
import com.pedro.clientesapi.entity.Item;
import com.pedro.clientesapi.exception.NotFoundException;
import com.pedro.clientesapi.repository.ItemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public List<ItemResponse> findAll() {
        return itemRepository.findAll().stream().map(ItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ItemResponse findById(@PathVariable Integer id) {
        return ItemResponse.from(getOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse create(@Valid @RequestBody ItemRequest request) {
        Item item = new Item();
        item.setName(request.name());
        item.setDescription(request.description());
        return ItemResponse.from(itemRepository.save(item));
    }

    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable Integer id, @Valid @RequestBody ItemRequest request) {
        Item item = getOrThrow(id);
        item.setName(request.name());
        item.setDescription(request.description());
        return ItemResponse.from(itemRepository.save(item));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Item nao encontrado: " + id);
        }
        itemRepository.deleteById(id);
    }

    private Item getOrThrow(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item nao encontrado: " + id));
    }
}
