package es.kyoe.ing.mortgage.persistency.h2.repository;

import es.kyoe.ing.mortgage.persistency.h2.entity.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MortgageRateRepository extends JpaRepository<RateEntity, Long> {

    Optional<RateEntity> findByIdApi(UUID idApi);

    Optional<RateEntity> findByMaturityPeriod(int maturityPeriod);
}
