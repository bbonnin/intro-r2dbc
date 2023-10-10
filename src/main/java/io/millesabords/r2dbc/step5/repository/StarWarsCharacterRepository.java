package io.millesabords.r2dbc.step5.repository;

import io.millesabords.r2dbc.step5.entity.StarWarsCharacter;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface StarWarsCharacterRepository extends R2dbcRepository<StarWarsCharacter, Integer> {
}
