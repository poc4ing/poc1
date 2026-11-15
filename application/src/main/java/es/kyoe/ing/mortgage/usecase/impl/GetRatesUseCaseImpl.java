package es.kyoe.ing.mortgage.usecase.impl;

import es.kyoe.ing.mortgage.Rate;
import es.kyoe.ing.mortgage.port.RatePersistencyPort;
import es.kyoe.ing.mortgage.usecase.GetRatesUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetRatesUseCaseImpl implements GetRatesUseCase {

    private final RatePersistencyPort ratePersistencyPort;

    public GetRatesUseCaseImpl(RatePersistencyPort ratePersistencyPort) {
        this.ratePersistencyPort = ratePersistencyPort;
    }

    @Override
    public Page<Rate> getRates(Pageable pageable) {
        return ratePersistencyPort.findAll(pageable);
    }
}

