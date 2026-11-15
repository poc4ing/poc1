package es.kyoe.ing.mortgage.usecase.impl;

import es.kyoe.ing.mortgage.MortgageCheck;
import es.kyoe.ing.mortgage.MortgageCheckResult;
import es.kyoe.ing.mortgage.Rate;
import es.kyoe.ing.mortgage.exception.MaturityPeriodNotFoundException;
import es.kyoe.ing.mortgage.port.RatePersistencyPort;
import es.kyoe.ing.mortgage.usecase.CheckMortgageUseCase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class CheckMortgageUseCaseImpl implements CheckMortgageUseCase {

    private static final BigDecimal INCOME_MULTIPLIER = BigDecimal.valueOf(4);
    private static final BigDecimal MONTHS = BigDecimal.valueOf(12);
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final RatePersistencyPort ratePersistencyPort;

    public CheckMortgageUseCaseImpl(RatePersistencyPort ratePersistencyPort) {
        this.ratePersistencyPort = ratePersistencyPort;
    }

    @Override
    public MortgageCheckResult checkMortgage(MortgageCheck check) {
        Rate rate = ratePersistencyPort.findByMaturityPeriod(check.getMaturityPeriod())
                .orElseThrow(() -> new MaturityPeriodNotFoundException(
                        "Maturity period not found: " + check.getMaturityPeriod()
                ));

        BigDecimal maxLoan = check.getIncome().multiply(INCOME_MULTIPLIER);
        boolean feasible = check.getLoanValue().compareTo(maxLoan) <= 0
                && check.getLoanValue().compareTo(check.getHomeValue()) <= 0;

        BigDecimal monthlyCosts = monthlyPayment(
                check.getLoanValue(),
                rate.getInterestRate(),
                check.getMaturityPeriod()
        );

        return new MortgageCheckResult(feasible, monthlyCosts);
    }

    private BigDecimal monthlyPayment(BigDecimal principal, BigDecimal annualRate, int years) {
        int n = years * 12;
        if (n <= 0 || principal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = annualRate
                .divide(HUNDRED, MathContext.DECIMAL128)
                .divide(MONTHS, MathContext.DECIMAL128);

        BigDecimal factor = BigDecimal.ONE
                .add(monthlyRate, MathContext.DECIMAL128)
                .pow(n, MathContext.DECIMAL128);

        BigDecimal numerator = principal
                .multiply(monthlyRate, MathContext.DECIMAL128)
                .multiply(factor, MathContext.DECIMAL128);

        BigDecimal denominator = factor.subtract(BigDecimal.ONE, MathContext.DECIMAL128);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
