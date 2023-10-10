package io.millesabords.r2dbc.step5.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("STAR_WARS_CHARACTER")
@Data
@Builder
public class StarWarsCharacter {

    @Id
    private Integer id;

    private String name;
}
