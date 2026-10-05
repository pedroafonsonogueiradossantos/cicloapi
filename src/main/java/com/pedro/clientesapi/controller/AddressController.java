package com.pedro.clientesapi.controller;

import com.pedro.clientesapi.dto.AddressRequest;
import com.pedro.clientesapi.dto.AddressResponse;
import com.pedro.clientesapi.entity.Address;
import com.pedro.clientesapi.entity.City;
import com.pedro.clientesapi.entity.Person;
import com.pedro.clientesapi.exception.NotFoundException;
import com.pedro.clientesapi.repository.AddressRepository;
import com.pedro.clientesapi.repository.CityRepository;
import com.pedro.clientesapi.repository.PersonRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;
    private final CityRepository cityRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public List<AddressResponse> findAll(@RequestParam(required = false) Integer personId) {
        List<Address> addresses = personId != null
                ? addressRepository.findByPersonId(personId)
                : addressRepository.findAll();
        return addresses.stream().map(AddressResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public AddressResponse findById(@PathVariable Integer id) {
        return AddressResponse.from(getOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse create(@Valid @RequestBody AddressRequest request) {
        Address address = new Address();
        applyRequest(address, request);
        return AddressResponse.from(addressRepository.save(address));
    }

    @PutMapping("/{id}")
    public AddressResponse update(@PathVariable Integer id, @Valid @RequestBody AddressRequest request) {
        Address address = getOrThrow(id);
        applyRequest(address, request);
        return AddressResponse.from(addressRepository.save(address));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!addressRepository.existsById(id)) {
            throw new NotFoundException("Endereco nao encontrado: " + id);
        }
        addressRepository.deleteById(id);
    }

    private void applyRequest(Address address, AddressRequest request) {
        Person person = personRepository.findById(request.personId())
                .orElseThrow(() -> new NotFoundException("Pessoa nao encontrada: " + request.personId()));
        address.setPerson(person);
        address.setStreet(request.street());
        address.setNumber(request.number());
        address.setNeighborhood(request.neighborhood());
        if (request.cityId() != null) {
            City city = cityRepository.findById(request.cityId())
                    .orElseThrow(() -> new NotFoundException("Cidade nao encontrada: " + request.cityId()));
            address.setCity(city);
        } else {
            address.setCity(null);
        }
    }

    private Address getOrThrow(Integer id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Endereco nao encontrado: " + id));
    }
}
