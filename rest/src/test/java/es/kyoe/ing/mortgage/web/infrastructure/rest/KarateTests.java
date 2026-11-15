package es.kyoe.ing.mortgage.web.infrastructure.rest;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {MortgageRestTestApp.class})
@ActiveProfiles("local")
class KarateTests {

    @LocalServerPort
    private int port;

    @Karate.Test
    Karate testAll() {
        return Karate.run("classpath:features")
                .systemProperty("karate.port", String.valueOf(port));
    }
}

