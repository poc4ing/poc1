package es.kyoe.ing.mortgage.web.infrastructure.rest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MortgageRestConfig.class)
public class MortgageRestTestApp {
}

