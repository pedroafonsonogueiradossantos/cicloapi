package com.pedro.clientesapi.controller;

import com.pedro.clientesapi.dto.PageResponse;
import com.pedro.clientesapi.dto.PersonRequest;
import com.pedro.clientesapi.dto.PersonResponse;
import com.pedro.clientesapi.entity.Person;
import com.pedro.clientesapi.exception.NotFoundException;
import com.pedro.clientesapi.repository.PersonRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping
    public PageResponse<PersonResponse> findAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<Person> persons = (name == null || name.isBlank())
                ? personRepository.findAll(pageable)
                : personRepository.findByNameContainingIgnoreCase(name, pageable);
        return PageResponse.from(persons.map(PersonResponse::from));
    }

    @GetMapping("/{id}")
    public PersonResponse findById(@PathVariable Integer id) {
        return PersonResponse.from(getOrThrow(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponse create(@Valid @RequestBody PersonRequest request) {
        Person person = new Person();
        person.setName(request.name());
        return PersonResponse.from(personRepository.save(person));
    }

    @PutMapping("/{id}")
    public PersonResponse update(@PathVariable Integer id, @Valid @RequestBody PersonRequest request) {
        Person person = getOrThrow(id);
        person.setName(request.name());
        return PersonResponse.from(personRepository.save(person));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!personRepository.existsById(id)) {
            throw new NotFoundException("Pessoa nao encontrada: " + id);
        }
        personRepository.deleteById(id);
    }

    private Person getOrThrow(Integer id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pessoa nao encontrada: " + id));
    }
}
