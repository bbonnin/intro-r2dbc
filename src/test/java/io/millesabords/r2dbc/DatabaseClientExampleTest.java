package io.millesabords.r2dbc;

import io.millesabords.r2dbc.step3.Example1DatabaseClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseClientExampleTest extends AbstractRobotAppTest {

    @Autowired
    private Example1DatabaseClient databaseClientExample;

    @Test
    public void findRobotsByName_whenSearchForR2D2_then1IsExpected() {
        databaseClientExample.findRobotsByName("R2-D2").log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
