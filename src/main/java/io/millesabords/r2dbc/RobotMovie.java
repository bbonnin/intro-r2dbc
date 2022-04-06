package io.millesabords.r2dbc;

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
public class RobotMovie {

    @Id
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "movie", referencedColumnName = "title")
    private Movie movie;
}
