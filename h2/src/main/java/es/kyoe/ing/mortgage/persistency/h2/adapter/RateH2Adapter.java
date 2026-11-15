package es.kyoe.ing.mortgage.persistency.h2.adapter;

import es.kyoe.ing.mortgage.Rate;
import es.kyoe.ing.mortgage.persistency.h2.mapper.MortgageRateEntityMapper;
import es.kyoe.ing.mortgage.persistency.h2.repository.MortgageRateRepository;
import es.kyoe.ing.mortgage.port.RatePersistencyPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RateH2Adapter implements RatePersistencyPort {

    private final MortgageRateRepository repository;
    private final MortgageRateEntityMapper mapper;

    public RateH2Adapter(MortgageRateRepository repository, MortgageRateEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Cacheable(
            cacheNames = "ratesPageCache",
            key = "#p0.pageNumber + '-' + #p0.pageSize + '-' + #p0.sort.toString()"
    )
    public Page<Rate> findAll(Pageable pageable) {
        Pageable effectivePageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Direction.ASC, "maturityPeriod")
                );
        return repository.findAll(effectivePageable)
                .map(mapper::toDomain);
    }

    @Override
    @Cacheable(cacheNames = "rateByMaturityCache", key = "#p0")
    public Optional<Rate> findByMaturityPeriod(int maturityPeriod) {
        return repository.findByMaturityPeriod(maturityPeriod)
                .map(mapper::toDomain);
    }
}

