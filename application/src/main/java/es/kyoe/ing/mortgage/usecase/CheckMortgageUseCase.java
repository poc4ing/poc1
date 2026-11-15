package es.kyoe.ing.mortgage.usecase;

import es.kyoe.ing.mortgage.MortgageCheck;
import es.kyoe.ing.mortgage.MortgageCheckResult;

public interface CheckMortgageUseCase {

    MortgageCheckResult checkMortgage(MortgageCheck mortgageCheck);
}

