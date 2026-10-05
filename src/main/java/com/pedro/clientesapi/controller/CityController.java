package com.pedro.clientesapi.controller;

import com.pedro.clientesapi.dto.CityRequest;
import com.pedro.clientesapi.dto.CityResponse;
import com.pedro.clientesapi.entity.City;
import com.pedro.clientesapi.exception.NotFoundException;
import com.pedro.clientesapi.repository.CityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityRepository cityRepository;

    @GetMapping
    public List<CityResponse> findAll() {
        return cityRepository.findAll().stream().map(CityResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CityResponse findById(@PathVariable Integer id) {
        return CityResponse.from(getOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityResponse create(@Valid @RequestBody CityRequest request) {
        City city = new City();
        city.setName(request.name());
        return CityResponse.from(cityRepository.save(city));
    }

    @PutMapping("/{id}")
    public CityResponse update(@PathVariable Integer id, @Valid @RequestBody CityRequest request) {
        City city = getOrThrow(id);
        city.setName(request.name());
        return CityResponse.from(cityRepository.save(city));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!cityRepository.existsById(id)) {
            throw new NotFoundException("Cidade nao encontrada: " + id);
        }
        cityRepository.deleteById(id);
    }

    private City getOrThrow(Integer id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cidade nao encontrada: " + id));
    }
}
