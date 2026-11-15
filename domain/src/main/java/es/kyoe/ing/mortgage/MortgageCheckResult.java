package es.kyoe.ing.mortgage;

import java.math.BigDecimal;
import java.util.Objects;

public class MortgageCheckResult {

    private boolean feasible;
    private BigDecimal monthlyCosts;

    public MortgageCheckResult() {
    }

    public MortgageCheckResult(boolean feasible, BigDecimal monthlyCosts) {
        this.feasible = feasible;
        this.monthlyCosts = monthlyCosts;
    }

    public boolean isFeasible() {
        return feasible;
    }

    public void setFeasible(boolean feasible) {
        this.feasible = feasible;
    }

    public BigDecimal getMonthlyCosts() {
        return monthlyCosts;
    }

    public void setMonthlyCosts(BigDecimal monthlyCosts) {
        this.monthlyCosts = monthlyCosts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MortgageCheckResult that)) return false;
        return feasible == that.feasible && Objects.equals(monthlyCosts, that.monthlyCosts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feasible, monthlyCosts);
    }

    @Override
    public String toString() {
        return "MortgageCheckResult{" +
                "feasible=" + feasible +
                ", monthlyCosts=" + monthlyCosts +
                '}';
    }
}

