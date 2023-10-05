package io.millesabords.r2dbc.step3.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Robot {

    @Id
    private Integer id;

    private String name;

    private String movie;

    private Integer firstMovie;

    public Robot(Integer id, String name, String movie) {
        this.id = id;
        this.name = name;
        this.movie = movie;
    }
}
