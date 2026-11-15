package es.kyoe.ing.mortgage.persistency.h2.config;

import es.kyoe.ing.mortgage.Rate;
import es.kyoe.ing.mortgage.persistency.h2.mapper.MortgageRateEntityMapper;
import es.kyoe.ing.mortgage.persistency.h2.repository.MortgageRateRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class MortgageRateDataInitializer implements ApplicationRunner {

    private final MortgageRateRepository repository;
    private final MortgageRateEntityMapper mapper;

    public MortgageRateDataInitializer(MortgageRateRepository repository, MortgageRateEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0L) {
            return;
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        List<Rate> rates = List.of(
                new Rate(1, new BigDecimal("2.50"), now),
                new Rate(5, new BigDecimal("3.00"), now),
                new Rate(10, new BigDecimal("3.50"), now),
                new Rate(15, new BigDecimal("3.75"), now),
                new Rate(20, new BigDecimal("4.00"), now),
                new Rate(25, new BigDecimal("4.25"), now),
                new Rate(30, new BigDecimal("4.50"), now)
        );
        repository.saveAll(rates.stream().map(mapper::toEntity).toList());
    }
}
