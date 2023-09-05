package io.millesabords.r2dbc.step3.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
