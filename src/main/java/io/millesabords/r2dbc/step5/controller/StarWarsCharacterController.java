package io.millesabords.r2dbc.step5.controller;

import io.millesabords.r2dbc.step5.entity.StarWarsCharacter;
import io.millesabords.r2dbc.step5.repository.StarWarsCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/starwars/character")
public class StarWarsCharacterController {

    private final StarWarsCharacterRepository repository;

    @PostMapping("/{character_name}")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable("character_name") String characterName) {
        repository.save(StarWarsCharacter.builder().name(characterName).build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<StarWarsCharacter> list() {
        return repository.findAll();
    }
}
