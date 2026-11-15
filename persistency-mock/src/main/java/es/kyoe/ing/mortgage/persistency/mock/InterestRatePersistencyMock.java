package es.kyoe.ing.mortgage.persistency.mock;

import es.kyoe.ing.mortgage.Rate;
import es.kyoe.ing.mortgage.port.RatePersistencyPort;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InterestRatePersistencyMock implements RatePersistencyPort {

    private final Map<Integer, Rate> ratesByMaturity = new ConcurrentHashMap<>();

    @PostConstruct
    public void datadump() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        List<Rate> initialRates = List.of(
                new Rate(UUID.ofEpochMillis(System.currentTimeMillis()), 1, new BigDecimal("2.50"), now),
                new Rate(UUID.ofEpochMillis(System.currentTimeMillis()), 5, new BigDecimal("3.00"), now),
                new Rate(UUID.ofEpochMillis(System.currentTimeMillis()), 10, new BigDecimal("3.50"), now),
                new Rate(UUID.ofEpochMillis(System.currentTimeMillis()), 15, new BigDecimal("3.75"), now),
                new Rate(UUID.ofEpochMillis(System.currentTimeMillis()), 20, new BigDecimal("4.00"), now),
                new Rate(UUID.ofEpochMillis(System.currentTimeMillis()), 25, new BigDecimal("4.25"), now),
                new Rate(UUID.ofEpochMillis(System.currentTimeMillis()), 30, new BigDecimal("4.50"), now)
        );
        for (Rate rate : initialRates) {
            ratesByMaturity.put(rate.getMaturityPeriod(), rate);
        }
    }

    @Override
    public Page<Rate> findAll(Pageable pageable) {
        List<Rate> sorted = ratesByMaturity.values().stream()
                .sorted(Comparator.comparingInt(Rate::getMaturityPeriod))
                .toList();
        if (pageable.isUnpaged()) {
            return new PageImpl<>(sorted, pageable, sorted.size());
        }
        int start = (int) Math.min(pageable.getOffset(), sorted.size());
        int end = Math.min(start + pageable.getPageSize(), sorted.size());
        return new PageImpl<>(sorted.subList(start, end), pageable, sorted.size());
    }

    @Override
    public Optional<Rate> findByMaturityPeriod(int maturityPeriod) {
        return Optional.ofNullable(ratesByMaturity.get(maturityPeriod));
    }
}
