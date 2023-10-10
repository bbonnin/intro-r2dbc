package io.millesabords.r2dbc.step3.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table("robot")
public class RobotWithMovie {

    @Id
    private Integer id;

    private String name;

    /* Does not work... */
    @OneToOne
    @JoinColumn(name = "movie", referencedColumnName = "title")
    private Movie movie;
}
