package es.kyoe.ing.mortgage.usecase;

import es.kyoe.ing.mortgage.Rate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetRatesUseCase {

    Page<Rate> getRates(Pageable pageable);
}

