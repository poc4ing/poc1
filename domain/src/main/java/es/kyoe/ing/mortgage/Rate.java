package es.kyoe.ing.mortgage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Rate implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private int maturityPeriod;
    private BigDecimal interestRate;
    private OffsetDateTime lastUpdate;

    public Rate() {
    }

    public Rate(int maturityPeriod, BigDecimal interestRate, OffsetDateTime lastUpdate) {
        this(UUID.ofEpochMillis(System.currentTimeMillis()), maturityPeriod, interestRate, lastUpdate);
    }

    public Rate(UUID id, int maturityPeriod, BigDecimal interestRate, OffsetDateTime lastUpdate) {
        this.id = id != null ? id : UUID.ofEpochMillis(System.currentTimeMillis());
        this.maturityPeriod = maturityPeriod;
        this.interestRate = interestRate;
        this.lastUpdate = lastUpdate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        if (!(o instanceof Rate that)) return false;
        return maturityPeriod == that.maturityPeriod
                && Objects.equals(id, that.id)
                && Objects.equals(interestRate, that.interestRate)
                && Objects.equals(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, maturityPeriod, interestRate, lastUpdate);
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", maturityPeriod=" + maturityPeriod +
                ", interestRate=" + interestRate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
