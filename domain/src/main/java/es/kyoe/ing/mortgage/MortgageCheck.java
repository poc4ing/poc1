package es.kyoe.ing.mortgage;

import java.math.BigDecimal;
import java.util.Objects;

public class MortgageCheck {

    private BigDecimal income;
    private int maturityPeriod;
    private BigDecimal loanValue;
    private BigDecimal homeValue;

    public MortgageCheck() {
    }

    public MortgageCheck(BigDecimal income, int maturityPeriod, BigDecimal loanValue, BigDecimal homeValue) {
        this.income = income;
        this.maturityPeriod = maturityPeriod;
        this.loanValue = loanValue;
        this.homeValue = homeValue;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public int getMaturityPeriod() {
        return maturityPeriod;
    }

    public void setMaturityPeriod(int maturityPeriod) {
        this.maturityPeriod = maturityPeriod;
    }

    public BigDecimal getLoanValue() {
        return loanValue;
    }

    public void setLoanValue(BigDecimal loanValue) {
        this.loanValue = loanValue;
    }

    public BigDecimal getHomeValue() {
        return homeValue;
    }

    public void setHomeValue(BigDecimal homeValue) {
        this.homeValue = homeValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MortgageCheck that)) return false;
        return maturityPeriod == that.maturityPeriod
                && Objects.equals(income, that.income)
                && Objects.equals(loanValue, that.loanValue)
                && Objects.equals(homeValue, that.homeValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(income, maturityPeriod, loanValue, homeValue);
    }

    @Override
    public String toString() {
        return "MortgageCheck{" +
                "income=" + income +
                ", maturityPeriod=" + maturityPeriod +
                ", loanValue=" + loanValue +
                ", homeValue=" + homeValue +
                '}';
    }
}

