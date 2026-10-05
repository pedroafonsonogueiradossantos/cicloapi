package com.pedro.clientesapi.controller;

import com.pedro.clientesapi.dto.PhoneRequest;
import com.pedro.clientesapi.dto.PhoneResponse;
import com.pedro.clientesapi.entity.Person;
import com.pedro.clientesapi.entity.Phone;
import com.pedro.clientesapi.entity.PhoneType;
import com.pedro.clientesapi.exception.NotFoundException;
import com.pedro.clientesapi.repository.PersonRepository;
import com.pedro.clientesapi.repository.PhoneRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phones")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneRepository phoneRepository;
    private final PersonRepository personRepository;

    @GetMapping
    public List<PhoneResponse> findAll(@RequestParam(required = false) Integer personId) {
        List<Phone> phones = personId != null
                ? phoneRepository.findByPersonId(personId)
                : phoneRepository.findAll();
        return phones.stream().map(PhoneResponse::from).toList();
    }

    @GetMapping("/{id}")
    public PhoneResponse findById(@PathVariable Integer id) {
        return PhoneResponse.from(getOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneResponse create(@Valid @RequestBody PhoneRequest request) {
        Phone phone = new Phone();
        applyRequest(phone, request);
        return PhoneResponse.from(phoneRepository.save(phone));
    }

    @PutMapping("/{id}")
    public PhoneResponse update(@PathVariable Integer id, @Valid @RequestBody PhoneRequest request) {
        Phone phone = getOrThrow(id);
        applyRequest(phone, request);
        return PhoneResponse.from(phoneRepository.save(phone));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!phoneRepository.existsById(id)) {
            throw new NotFoundException("Telefone nao encontrado: " + id);
        }
        phoneRepository.deleteById(id);
    }

    private void applyRequest(Phone phone, PhoneRequest request) {
        Person person = personRepository.findById(request.personId())
                .orElseThrow(() -> new NotFoundException("Pessoa nao encontrada: " + request.personId()));
        phone.setPerson(person);
        phone.setNumber(request.number());
        phone.setType(request.type() != null ? request.type() : PhoneType.mobile);
    }

    private Phone getOrThrow(Integer id) {
        return phoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Telefone nao encontrado: " + id));
    }
}
