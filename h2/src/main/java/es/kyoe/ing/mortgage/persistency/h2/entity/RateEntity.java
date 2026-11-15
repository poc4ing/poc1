package es.kyoe.ing.mortgage.persistency.h2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "RATES")
public class RateEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rates_seq")
    @SequenceGenerator(name = "rates_seq", sequenceName = "RATES_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "ID_API", nullable = false, unique = true, updatable = false)
    private UUID idApi;

    @Column(name = "MATURITY_PERIOD", nullable = false, unique = true)
    private int maturityPeriod;

    @Column(name = "INTEREST_RATE", nullable = false, precision = 10, scale = 4)
    private BigDecimal interestRate = BigDecimal.ZERO;

    @Column(name = "LAST_UPDATE", nullable = false)
    private OffsetDateTime lastUpdate = OffsetDateTime.now(ZoneOffset.UTC);

    public RateEntity() {
    }

    public RateEntity(Long id, UUID idApi, int maturityPeriod, BigDecimal interestRate, OffsetDateTime lastUpdate) {
        this.id = id;
        this.idApi = idApi != null ? idApi : UUID.ofEpochMillis(System.currentTimeMillis());
        this.maturityPeriod = maturityPeriod;
        this.interestRate = interestRate;
        this.lastUpdate = lastUpdate;
    }

    @PrePersist
    public void ensureIdApi() {
        if (this.idApi == null) {
            this.idApi = UUID.ofEpochMillis(System.currentTimeMillis());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getIdApi() {
        return idApi;
    }

    public void setIdApi(UUID idApi) {
        this.idApi = idApi;
    }

    public int getMaturityPeriod() {
        return maturityPeriod;
    }

    public void setMaturityPeriod(int maturityPeriod) {
        this.maturityPeriod = maturityPeriod;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public OffsetDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(OffsetDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateEntity that)) return false;
        return maturityPeriod == that.maturityPeriod
                && Objects.equals(id, that.id)
                && Objects.equals(idApi, that.idApi)
                && Objects.equals(interestRate, that.interestRate)
                && Objects.equals(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idApi, maturityPeriod, interestRate, lastUpdate);
    }
}

