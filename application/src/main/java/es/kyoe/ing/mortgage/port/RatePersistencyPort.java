package es.kyoe.ing.mortgage.port;

import es.kyoe.ing.mortgage.Rate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RatePersistencyPort {

    Page<Rate> findAll(Pageable pageable);

    Optional<Rate> findByMaturityPeriod(int maturityPeriod);
}

