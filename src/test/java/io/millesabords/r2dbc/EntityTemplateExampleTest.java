package io.millesabords.r2dbc;

import io.millesabords.r2dbc.step3.Example2R2dbcEntityTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityTemplateExampleTest extends AbstractRobotAppTest {

    @Autowired
    private Example2R2dbcEntityTemplate entityTemplateExample;

    @Test
    public void findRobotsByNameV1_whenSearchForC3PO_then1IsExpected() {
        entityTemplateExample.findRobotsByNameV1("C-3PO").log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findRobotsByNameV2_whenSearchForC3PO_then1IsExpected() {
        entityTemplateExample.findRobotsByNameV2("C-3PO").log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void deleteRobotsOfMovie_whenDeleteStarWars_then3IsExpected() {
        entityTemplateExample.deleteRobotsOfMovie("Star Wars").log()
                .as(StepVerifier::create)
                .expectNext(3L)
                .verifyComplete();
    }
}
