package es.kyoe.ing.mortgage.exception;

public class MaturityPeriodNotFoundException extends MortgageException {

    public static final String CODE = "3003";

    public MaturityPeriodNotFoundException() {
        super();
    }

    public MaturityPeriodNotFoundException(String message) {
        super(message);
    }

    public MaturityPeriodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

